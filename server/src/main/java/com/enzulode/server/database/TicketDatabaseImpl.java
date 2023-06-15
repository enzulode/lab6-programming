package com.enzulode.server.database;

import com.enzulode.models.Coordinates;
import com.enzulode.models.Ticket;
import com.enzulode.models.Venue;
import com.enzulode.models.util.TicketType;
import com.enzulode.models.util.VenueType;
import com.enzulode.server.database.connection.DatabaseConnectionManager;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.database.loading.LoadingService;
import com.enzulode.server.database.loading.exception.LoadingException;
import com.enzulode.server.database.util.DatabaseUtils;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Ticket database implementation
 *
 */
public class TicketDatabaseImpl extends AbstractDatabase<Ticket> implements TicketDatabase
{
	/**
	 * Ticket database constructor
	 *
	 * @param connectionManager connection manager instance
	 * @param loadingService database loading service instance
	 * @throws DatabaseException if ddl init failed
	 */
	public TicketDatabaseImpl(
			@NonNull DatabaseConnectionManager connectionManager,
			@NonNull LoadingService<Ticket> loadingService
	) throws DatabaseException
	{
		super(connectionManager, loadingService);
		initDDLIfNotExists();
		load();
	}

	/**
	 * This method inits default database ddl
	 *
	 * @throws DatabaseException if database ddl initiation failed due to networking issues
	 */
	@Override
	public void initDDLIfNotExists() throws DatabaseException
	{
		try(var conn = connectionManager.getConnection())
		{
			conn.setAutoCommit(false);

			PreparedStatement ddlCoordinates = conn.prepareStatement("""
				CREATE TABLE IF NOT EXISTS coordinates(
					coordinates_id BIGSERIAL PRIMARY KEY,
					x_coord FLOAT NOT NULL,
					y_coord INTEGER NOT NULL
				)
			""");

			PreparedStatement ddlVenue = conn.prepareStatement("""
				CREATE TABLE IF NOT EXISTS venue(
					venue_id BIGSERIAL PRIMARY KEY,
					venue_name VARCHAR NOT NULL,
					venue_capacity INTEGER NOT NULL,
					venue_type VARCHAR
				)
			""");

			PreparedStatement ddlTicket = conn.prepareStatement("""
				CREATE TABLE IF NOT EXISTS ticket(
					ticket_id SERIAL PRIMARY KEY,
					ticket_name VARCHAR NOT NULL,
					coordinates BIGINT REFERENCES coordinates ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
					ticket_creation_date TIMESTAMP NOT NULL,
					ticket_price FLOAT NOT NULL,
					ticket_comment VARCHAR NOT NULL,
					ticket_refundable BOOLEAN NOT NULL,
					ticket_type VARCHAR,
					venue BIGINT REFERENCES venue ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
					)
			""");

			PreparedStatement ddlMetadata = conn.prepareStatement("""
				CREATE TABLE IF NOT EXISTS ticket_database_metadata(
					database_creation_date TIMESTAMP NOT NULL,
					database_type VARCHAR NOT NULL
				)
			""");

			ddlCoordinates.executeUpdate();
			ddlVenue.executeUpdate();
			ddlTicket.executeUpdate();
			ddlMetadata.executeUpdate();

			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (SQLTimeoutException e)
		{
			throw new DatabaseException("Failed to execute ddl statements (init tables)", e);
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to prepare statement", e);
		}
	}

	/**
	 * This method retrieves current database size
	 *
	 * @return current database size
	 */
	@Override
	public int size()
	{
		return elements.size();
	}

	/**
	 * This method retrieves database creation date
	 *
	 * @return database creation date
	 */
	@Override
	public LocalDateTime getCreationDate()
	{
		return creationDate;
	}

	/**
	 * This method loads the database
	 *
	 * @throws DatabaseException if database loading failed due to network reasons
	 */
	@Override
	public void load() throws DatabaseException
	{
		try
		{
			loadingService.load(this);
		}
		catch (LoadingException e)
		{
			throw new DatabaseException("Failed to load the database", e);
		}
	}

	/**
	 * This method clears the database
	 *
	 * @throws DatabaseException if database clearing failed
	 */
	@Override
	public void clear() throws DatabaseException
	{
		try(var conn = connectionManager.getConnection())
		{
			DatabaseUtils.truncateTicket(conn);
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to clear the database", e);
		}
	}

	/**
	 * This method inserts an object into the database
	 *
	 * @param object object to be inserted
	 * @throws DatabaseException if insertion failed
	 */
	@Override
	public void create(@NonNull Ticket object) throws DatabaseException
	{
		try(var conn = connectionManager.getConnection())
		{
			conn.setAutoCommit(false);
			DatabaseUtils.insertTicket(conn, object);
			conn.commit();
			conn.setAutoCommit(true);
			load();
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to create new ticket in the database", e);
		}
	}

	/**
	 * This method retrieves an object by id
	 *
	 * @param id object id
	 * @return an optional object value
	 */
	@Override
	public Optional<Ticket> read(@NonNull Integer id) throws DatabaseException
	{
		return elements
				.stream()
				.filter((el) -> id.equals(el.getId())).findFirst();
	}

	/**
	 * This method updates an already stored object
	 *
	 * @param id     updatable object id
	 * @param object object to be updated
	 * @throws DatabaseException if update failed
	 */
	@Override
	public void update(@NonNull Integer id, @NonNull Ticket object) throws DatabaseException
	{
		Optional<Ticket> optionalTicket = elements
				.stream()
				.filter(ticket -> id.equals(ticket.getId()))
				.findFirst();

        if (optionalTicket.isEmpty())
            throw new DatabaseException("Element with id " + id + " not found");

        Ticket existingTicket = optionalTicket.get();

        String ticketName = (object.getName() == null)
		        ? existingTicket.getName()
		        : object.getName();


        Float xCoord;
		Integer yCoord;
		if (object.getCoordinates() == null)
		{
			xCoord = existingTicket.getCoordinates().getX();
			yCoord = existingTicket.getCoordinates().getY();
		}
		else
		{
			xCoord = (object.getCoordinates().getX() == null)
		        ? existingTicket.getCoordinates().getX()
		        : object.getCoordinates().getX();

			yCoord = (object.getCoordinates().getY() == null)
		        ? existingTicket.getCoordinates().getY()
		        : object.getCoordinates().getY();
		}

        LocalDateTime ticketCreationDate = existingTicket.getCreationDate();

        Float price = (object.getPrice() == null)
		        ? existingTicket.getPrice()
		        : object.getPrice();

        String comment = (object.getComment() == null)
		        ? existingTicket.getComment()
		        : object.getComment();

        Boolean refundable = (object.getRefundable() == null)
		        ? existingTicket.getRefundable()
		        : object.getRefundable();

        TicketType ticketType = (object.getType() == null)
		        ? existingTicket.getType()
		        : object.getType();

		String venueName;
		Integer venueCapacity;
		VenueType venueType;
		if (object.getVenue() == null)
		{
			venueName = existingTicket.getVenue().getName();
			venueCapacity = existingTicket.getVenue().getCapacity();
			venueType = existingTicket.getVenue().getType();
		}
		else
		{
			venueName = (object.getVenue().getName() == null)
					? existingTicket.getVenue().getName()
					: object.getVenue().getName();

			venueCapacity = (object.getVenue().getCapacity() == null)
		        ? existingTicket.getVenue().getCapacity()
		        : object.getVenue().getCapacity();

			venueType = (object.getVenue().getType() == null)
		        ? existingTicket.getVenue().getType()
		        : object.getVenue().getType();
		}

        Coordinates coordinates = new Coordinates(xCoord, yCoord);
        Venue venue = new Venue(existingTicket.getVenue().getId(), venueName, venueCapacity, venueType);

        Ticket newTicket = new Ticket(
				id,
		        ticketName,
		        coordinates,
		        ticketCreationDate,
		        price,
		        comment,
		        refundable,
		        ticketType,
		        venue
        );

		try(var conn = connectionManager.getConnection())
		{
			conn.setAutoCommit(false);
			DatabaseUtils.updateTicketById(conn, id, newTicket);
			conn.commit();
			conn.setAutoCommit(true);
			Optional<Ticket> notUpdatedTicket = read(id);

			if (notUpdatedTicket.isEmpty())
				throw new DatabaseException("Failed to update the database: database miscommunication");

			int prevIndex = elements.indexOf(notUpdatedTicket.get());
			elements.set(prevIndex, newTicket);
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to update an element: remote database inaccessible", e);
		}
	}

	/**
	 * This method deletes an existing object
	 *
	 * @param id deleted object id
	 * @throws DatabaseException if deletion failed
	 */
	@Override
	public void delete(@NonNull Integer id) throws DatabaseException
	{
		try(var conn = connectionManager.getConnection())
		{
			DatabaseUtils.deleteTicketById(conn, id);
			Optional<Ticket> optionalTicket = elements
					.stream()
					.filter((el) -> !id.equals(el.getId()))
					.findFirst();

			if (optionalTicket.isEmpty())
				throw new DatabaseException("Failed to remove ticket: ticket does not exist");

			Ticket ticket = optionalTicket.get();
			DatabaseUtils.deleteTicketById(conn, ticket.getId());
			elements.remove(ticket);
		}
		catch (SQLException e)
		{
			throw new DatabaseException("Failed to delete a ticket from the remote database", e);
		}

	}

	/**
	 * This method retrieves all stored tickets from the database
	 *
	 * @return a list of stored elements
	 */
	@Override
	public List<Ticket> findAll()
	{
		return elements;
	}

	/**
	 * This method deletes first stored element
	 *
	 * @throws DatabaseException if deletion failed
	 */
	@Override
	public void removeFirst() throws DatabaseException
	{
		if (elements.size() == 0)
			throw new DatabaseException("Element deletion failed: no such element - database is empty");

		delete(elements.get(0).getId());
	}

	/**
	 * This method deletes last stored element
	 *
	 * @throws DatabaseException if deletion failed
	 */
	@Override
	public void removeLast() throws DatabaseException
	{
		if (elements.size() == 0)
			throw new DatabaseException("Element deletion failed: no such element - database is empty");

		delete(elements.get(elements.size() - 1).getId());
	}

	/**
	 * This method deletes from the database all tickets lower that provided one
	 *
	 * @param object ticket for comparison
	 * @throws DatabaseException if tickets deletion failed
	 */
	@Override
	public void removeLower(@NonNull Ticket object) throws DatabaseException
	{
		List<Integer> removedIds = elements
				.stream()
				.filter(ticket -> ticket.getId() != null)
				.filter(ticket -> ticket.compareTo(object) < 0)
				.map(Ticket::getId)
				.toList();

		for (Integer id : removedIds)
			delete(id);
	}

	/**
	 * This method deletes one ticket with provided refundable status
	 *
	 * @param refundable refundable status
	 * @throws DatabaseException if deletion failed
	 */
	@Override
	public void removeAnyByRefundable(@NonNull Boolean refundable) throws DatabaseException
	{
		Optional<Ticket> ticketToBeRemoved = elements
				.stream()
				.filter(ticket -> ticket.getRefundable().booleanValue() == refundable.booleanValue())
				.findFirst();

        if (ticketToBeRemoved.isEmpty())
            throw new DatabaseException("There are no elements to be removed");

		delete(ticketToBeRemoved.get().getId());
	}

	/**
	 * This method retrieves a list of tickets with type less than provided
	 *
	 * @param type ticket type
	 * @return a list of tickets with type less than provided one
	 */
	@Override
	public List<Ticket> findLessThanType(@NonNull TicketType type)
	{
		return elements
				.stream()
				.filter(ticket -> (ticket.getType() == null || ticket.getType().compareTo(type) > 0))
				.toList();
	}

	/**
	 * This method checks id presence in the database
	 *
	 * @param id an id to be checked
	 * @return true if id is present and false if is not
	 */
	@Override
	public boolean checkIdExistence(@NonNull Integer id)
	{
		return elements
				.stream()
				.anyMatch(el -> id.equals(el.getId()));
	}
}
