package com.enzulode.server.database.util;

import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.validation.TicketValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Coordinates;
import com.enzulode.models.Ticket;
import com.enzulode.models.Venue;
import com.enzulode.models.util.TicketType;
import com.enzulode.models.util.VenueType;
import com.enzulode.server.database.exception.DatabaseException;
import lombok.NonNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a set of utility methods for remote database data manipulation
 *
 */
public class DatabaseUtils
{
	/**
	 * This method retrieves all remotely stored ticket from the database
	 *
	 * @param connection remote database connection instance
 	 * @return a list of valid remotely stored tickets
	 * @throws DatabaseException if it's failed to select tickets from the database or database structure has invalid
	 * labels
	 */
	public static List<Ticket> selectAllTickets(@NonNull Connection connection) throws DatabaseException
	{
		try
		{
			var selectAllTickets = connection.prepareStatement("""
				SELECT * FROM ticket
				INNER JOIN coordinates ON ticket.coordinates = coordinates.coordinates_id
				INNER JOIN venue ON ticket.venue = venue.venue_id
			""");

			List<Ticket> tickets = new ArrayList<>();
			var selectResult = selectAllTickets.executeQuery();

			while (selectResult.next())
			{
				try
				{
//					Retrieve coordinates from result set
					float xCoord = selectResult.getFloat("x_coord");
					int yCoord = selectResult.getInt("y_coord");
					var coordinates = new Coordinates(xCoord, yCoord);

//					Retrieve venue from result set
					long venueId = selectResult.getLong("venue_id");
					String venueName = selectResult.getString("venue_name");
					int venueCapacity = selectResult.getInt("venue_capacity");
					VenueType venueType = (selectResult.getString("venue_type") == null)
							? null
							: TypesParser.parseVenueType(selectResult.getString("venue_type"));
					var venue = new Venue(venueId, venueName, venueCapacity, venueType);

//					Retrieve ticket from result set
					int ticketId = selectResult.getInt("ticket_id");
					String ticketName = selectResult.getString("ticket_name");
					LocalDateTime ticketCreationDate = selectResult
							.getTimestamp("ticket_creation_date")
							.toLocalDateTime();
					float ticketPrice = selectResult.getFloat("ticket_price");
					String ticketComment = selectResult.getString("ticket_comment");
					boolean ticketRefundable = selectResult.getBoolean("ticket_refundable");
					TicketType ticketType = (selectResult.getString("ticket_type") == null)
							? null
							: TypesParser.parseTicketType(selectResult.getString("ticket_type"));

					var ticket = new Ticket(
							ticketId,
							ticketName,
							coordinates,
							ticketCreationDate,
							ticketPrice,
							ticketComment,
							ticketRefundable,
							ticketType,
							venue
					);

//					Validate ticket and add it to result if validation succeed
					TicketValidator.validateTicket(ticket);
					tickets.add(ticket);
				}
				catch (ParsingException | ValidationException ignore)
				{
//					Ignoring invalid elements
				}
				catch (SQLException e)
				{
					throw new DatabaseException("Failed to retrieve data: some of labels is invalid", e);
				}
			}

			return tickets;
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to select all tickets", e);
		}
	}

	/**
	 * This method inserts a coordinates object into the database
	 *
	 * @param connection remote database connection instance
	 * @param coordinates coordinates object to be inserted
	 * @return inserted object id
	 * @throws DatabaseException if coordinates object insertion failed
	 */
	public static long insertCoordinates(@NonNull Connection connection, @NonNull Coordinates coordinates) throws DatabaseException
	{
		try
		{
			var insertCoordinates = connection.prepareStatement("""
				INSERT INTO coordinates (x_coord, y_coord)
				VALUES (?, ?)
			""", Statement.RETURN_GENERATED_KEYS);

			insertCoordinates.setFloat(1, coordinates.getX());
			insertCoordinates.setInt(2, coordinates.getY());
			insertCoordinates.executeUpdate();

			if (!insertCoordinates.getGeneratedKeys().next())
				throw new DatabaseException("Coordinates insertion failed: 0 rows were affected");

			return insertCoordinates.getGeneratedKeys().getLong("coordinates_id");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to insert coordinates", e);
		}
	}

	/**
	 * This method inserts a venue object into the database
	 *
	 * @param connection remote database connection instance
	 * @param venue venue object to be inserted
	 * @return inserted object id
	 * @throws DatabaseException if venue object insertion failed
	 */
	public static long insertVenue(@NonNull Connection connection, @NonNull Venue venue) throws DatabaseException
	{
		try
		{
			var insertVenue = connection.prepareStatement("""
				INSERT INTO venue (venue_name, venue_capacity, venue_type)
				VALUES (?, ?, ?)
			""", Statement.RETURN_GENERATED_KEYS);

			insertVenue.setString(1, venue.getName());
			insertVenue.setInt(2, venue.getCapacity());
			insertVenue.setString(3, venue.getType().toString());

			insertVenue.executeUpdate();

			if (!insertVenue.getGeneratedKeys().next())
				throw new DatabaseException("Venue insertion: 0 rows affected");

			long insertedVenueId = insertVenue.getGeneratedKeys().getLong("venue_id");
			venue.setId(insertedVenueId);
			return insertedVenueId;
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to insert venue", e);
		}
	}

	/**
	 * This method inserts a ticket object into the database
	 *
	 * @param connection remote database connection instance
	 * @param ticket ticket object to be inserted
	 * @return inserted object id
	 * @throws DatabaseException if ticket object insertion failed
	 */
	public static int insertTicket(@NonNull Connection connection, @NonNull Ticket ticket) throws DatabaseException
	{
		try
		{
			long coordId = insertCoordinates(connection, ticket.getCoordinates());
			long venueId = insertVenue(connection, ticket.getVenue());

			var insertTicket = connection.prepareStatement("""
				INSERT INTO ticket
				(ticket_name, coordinates, ticket_creation_date, ticket_price, ticket_comment, ticket_refundable, ticket_type, venue)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""", Statement.RETURN_GENERATED_KEYS);

			insertTicket.setString(1, ticket.getName());
			insertTicket.setLong(2, coordId);
			insertTicket.setTimestamp(3, Timestamp.valueOf(ticket.getCreationDate()));
			insertTicket.setFloat(4, ticket.getPrice());
			insertTicket.setString(5, ticket.getComment());
			insertTicket.setBoolean(6, ticket.getRefundable());
			insertTicket.setString(7, ticket.getType().toString());
			insertTicket.setLong(8, venueId);

			insertTicket.executeUpdate();

			if (!insertTicket.getGeneratedKeys().next())
				throw new DatabaseException("Ticket insertion failed: 0 affected rows");

			int ticketId = insertTicket.getGeneratedKeys().getInt("ticket_id");
			ticket.setId(ticketId);
			return ticketId;
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to insert a ticket", e);
		}
	}

	/**
	 * This method deletes a ticket from the database
	 *
	 * @param connection remote database connection instance
	 * @param id ticket id to be deleted
	 * @throws DatabaseException if ticket deletion failed
	 */
	public static void deleteTicketById(@NonNull Connection connection, @NonNull Integer id) throws DatabaseException
	{
		try
		{
			PreparedStatement deleteTicket = connection.prepareStatement("DELETE FROM ticket WHERE ticket_id = ?");
			deleteTicket.setInt(1, id);
			int affected = deleteTicket.executeUpdate();

			if (affected == 0)
				throw new DatabaseException("Ticket deletion failed: 0 rows affected");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to delete a ticket", e);
		}
	}

	/**
	 * This method selects a coordinates id for specific ticket id
	 *
	 * @param connection database connection instance
	 * @param id ticket id
	 * @return coordinates id
	 * @throws DatabaseException if selection failed
	 */
	public static long selectCoordinatesIdForTicketById(@NonNull Connection connection, @NonNull Integer id) throws DatabaseException
	{
		try
		{
			var selectCoordId = connection.prepareStatement("""
                SELECT coordinates
                FROM ticket
                WHERE ticket_id = ?
            """);

			selectCoordId.setInt(1, id);

			var selectionResult = selectCoordId.executeQuery();

			if (!selectionResult.next())
				throw new DatabaseException("Failed to retrieve coordinates id for specific ticket id");

			return selectionResult.getLong("coordinates");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to retrieve information", e);
		}
	}

	/**
	 * This method updates coordinates by id
	 *
	 * @param connection database connection instance
	 * @param id coordinates id
	 * @param coordinates coordinates instance
	 * @throws DatabaseException if update failed
	 */
	public static void updateCoordinatesById(@NonNull Connection connection, @NonNull Long id, @NonNull Coordinates coordinates) throws DatabaseException
	{
		try
		{
			var updateCoordinates = connection.prepareStatement("""
				UPDATE coordinates
				SET x_coord = ?, y_coord = ?
				WHERE coordinates_id = ?
			""", Statement.RETURN_GENERATED_KEYS);

			updateCoordinates.setFloat(1, coordinates.getX());
			updateCoordinates.setInt(2, coordinates.getY());
			updateCoordinates.setLong(3, id);

			updateCoordinates.executeUpdate();

			if (!updateCoordinates.getGeneratedKeys().next())
				throw new DatabaseException("Coordinates update failed: 0 rows were affected");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to update coordinates", e);
		}
	}

	/**
	 * This method updates venue by id
	 *
	 * @param connection database connection instance
	 * @param id venue id
	 * @param venue venue instance
	 * @throws DatabaseException if update failed
	 */
	public static void updateVenueById(@NonNull Connection connection, @NonNull Long id, @NonNull Venue venue) throws DatabaseException
	{
		try
		{
			var updateVenue = connection.prepareStatement("""
				UPDATE venue
				SET venue_name = ?, venue_capacity = ?, venue_type = ?
				WHERE venue_id = ?
			""", Statement.RETURN_GENERATED_KEYS);

			updateVenue.setString(1, venue.getName());
			updateVenue.setInt(2, venue.getCapacity());
			updateVenue.setString(3, venue.getType().toString());
			updateVenue.setLong(4, id);

			updateVenue.executeUpdate();

			if (!updateVenue.getGeneratedKeys().next())
				throw new DatabaseException("Venue update failed: 0 rows were affected");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to update venue", e);
		}
	}

	/**
	 * This method updates ticket by id
	 *
	 * @param connection database connection instance
	 * @param id ticket id
	 * @param ticket ticket instance
	 * @throws DatabaseException if update failed
	 */
	public static void updateTicketById(@NonNull Connection connection, @NonNull Integer id, @NonNull Ticket ticket) throws DatabaseException
	{
		try
		{
			long coordsId = selectCoordinatesIdForTicketById(connection, id);
			updateCoordinatesById(connection, coordsId, ticket.getCoordinates());
			updateVenueById(connection, ticket.getVenue().getId(), ticket.getVenue());

			var updateTicket = connection.prepareStatement("""
				UPDATE ticket
				SET ticket_name = ?, ticket_creation_date = ?, ticket_price = ?, ticket_comment = ?, ticket_refundable = ?, ticket_type = ?
				WHERE ticket_id = ?
			""", Statement.RETURN_GENERATED_KEYS);

			updateTicket.setString(1, ticket.getName());
			updateTicket.setTimestamp(2, Timestamp.valueOf(ticket.getCreationDate()));
			updateTicket.setFloat(3, ticket.getPrice());
			updateTicket.setString(4, ticket.getComment());
			updateTicket.setBoolean(5, ticket.getRefundable());
			updateTicket.setString(6, ticket.getType().toString());
			updateTicket.setInt(7, id);

			updateTicket.executeUpdate();

			if (!updateTicket.getGeneratedKeys().next())
				throw new DatabaseException("Ticket update failed: 0 rows were affected");
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to update ticket", e);
		}
	}

	/**
	 * This method truncates coordinates
	 *
	 * @param connection database connection instance
	 * @throws DatabaseException if truncate failed
	 */
	public static void truncateCoordinates(@NonNull Connection connection) throws DatabaseException
	{
		try
		{
			var truncateCoords = connection.prepareStatement("TRUNCATE coordinates CASCADE");
			truncateCoords.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to truncate coordinates", e);
		}
	}

	/**
	 * This method truncates venue
	 *
	 * @param connection database connection instance
	 * @throws DatabaseException if truncate failed
	 */
	public static void truncateVenue(@NonNull Connection connection) throws DatabaseException
	{
		try
		{
			var truncateVenue = connection.prepareStatement("TRUNCATE venue CASCADE");
			truncateVenue.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to truncate venue", e);
		}
	}

	/**
	 * This method truncates ticket
	 *
	 * @param connection database connection instance
	 * @throws DatabaseException if truncate failed
	 */
	public static void truncateTicket(@NonNull Connection connection) throws DatabaseException
	{
		try
		{
			truncateCoordinates(connection);
			truncateVenue(connection);

			var truncateTicket = connection.prepareStatement("TRUNCATE ticket CASCADE");
			truncateTicket.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to truncate ticket", e);
		}
	}
}
