package com.enzulode.server.database;

import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.filesystem.exception.FileException;
import com.enzulode.common.filesystem.exception.FileNotExistsException;
import com.enzulode.models.Coordinates;
import com.enzulode.models.Ticket;
import com.enzulode.models.Venue;
import com.enzulode.models.util.LocalDateTimeAdapter;
import com.enzulode.models.util.TicketType;
import com.enzulode.models.util.VenueType;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.database.exception.DatabaseLoadingFailedException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/** Database implementation for storing tickets */
@NoArgsConstructor(force = true)
@Getter
@XmlRootElement(name = "database")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketDatabaseImpl implements Database<Ticket>
{
    /** The database creation date */
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	@XmlElement(name = "dbCreationDate", required = true)
    private LocalDateTime creationDate;

    /** A storage for all the tickets */
	@XmlElementWrapper(name = "elements", required = true)
	@XmlElement(name = "element")
    private List<Ticket> elements;

	@XmlElement
	private int lastTicketId = 0;

	@XmlElement
	private long lastVenueId = 0;

	@XmlTransient
    /** Database saving service instance */
    private final TicketDatabaseSavingService savingService;

	@XmlTransient
	private final File savingLoadingFile;

	@XmlTransient
	private final FileManipulationService fms;

    /**
     * TicketDatabaseImpl constructor
     *
     * @param savingService database saving service instance
     */
    public TicketDatabaseImpl(
			TicketDatabaseSavingService savingService,
			FileManipulationService fms,
			File savingLoadingFile
    )
    {
        initEmptyDb();
        this.savingService = savingService;
		this.savingLoadingFile = savingLoadingFile;
		this.fms = fms;
    }

    /**
     * This method retrieves amount of stored elements
     *
     * @return amount of stored elements
     */
    @Override
    public int size()
    {
        return elements.size();
    }

    /**
     * This method retrieves creation date of the database
     *
     * @return database creation date
     */
    @Override
    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

	/**
	 * Database creation date setter
	 *
	 * @param creationDate database creation date to be set
	 */
	@Override
	public void setCreationDate(@NonNull LocalDateTime creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
     * This method retrieves all elements from the database
     *
     * @return list of stored elements
     */
    @Override
    public List<Ticket> findAll()
    {
        return elements;
    }

	/**
	 * Elements setter
	 *
	 * @param elements elements property to be set
	 */
	@Override
	public void setElements(List<Ticket> elements)
	{
		this.elements.clear();
		this.elements = new ArrayList<>(elements);
	}

	/**
     * This method retrieves element stored in the database by id
     *
     * @param id stored element id
     * @return element
     * @throws DatabaseException if the specified id does not exist in the database
     */
    @Override
    public Ticket findElementById(@NonNull Integer id) throws DatabaseException
    {
        Optional<Ticket> optionalTicket =
                elements.stream().filter((ticket) -> id.equals(ticket.getId())).findFirst();

        if (optionalTicket.isEmpty()) throw new DatabaseException("Element with specified id not found");

        return optionalTicket.get();
    }

    /**
     * This method provides an ability to add new element into the database
     *
     * @param item the new element which should be added into the database
     */
    @Override
    public void add(@NonNull Ticket item)
    {
		item.setId(++lastTicketId);
		item.getVenue().setId(++lastVenueId);

        elements.add(item);
    }

    /**
     * This method provides an ability to update the existing element by id
     *
     * @param id existing element id
     * @param item element that the existing element will be updated with
     * @throws DatabaseException if the specified id does not exist in the database
     */
    @Override
    public void update(@NonNull Integer id, @NonNull Ticket item) throws DatabaseException
    {
        Optional<Ticket> optionalTicket =
                elements.stream().filter(ticket -> id.equals(ticket.getId())).findFirst();

        if (optionalTicket.isEmpty())
            throw new DatabaseException("Element with id " + id + " not found");

        Ticket existingTicket = optionalTicket.get();

        String ticketName = (item.getName() == null) ? existingTicket.getName() : item.getName();
        Float xCoord =
                (item.getCoordinates().getX() == null)
                        ? existingTicket.getCoordinates().getX()
                        : item.getCoordinates().getX();
        Integer yCoord =
                (item.getCoordinates().getY() == null)
                        ? existingTicket.getCoordinates().getY()
                        : item.getCoordinates().getY();
        LocalDateTime ticketCreationDate = existingTicket.getCreationDate();
        Float price = (item.getPrice() == null) ? existingTicket.getPrice() : item.getPrice();
        String comment =
                (item.getComment() == null) ? existingTicket.getComment() : item.getComment();
        Boolean refundable =
                (item.getRefundable() == null)
                        ? existingTicket.getRefundable()
                        : item.getRefundable();
        TicketType ticketType =
                (item.getType() == null) ? existingTicket.getType() : item.getType();

        String venueName =
                (item.getVenue().getName() == null)
                        ? existingTicket.getVenue().getName()
                        : item.getVenue().getName();
        Integer venueCapacity =
                (item.getVenue().getCapacity() == null)
                        ? existingTicket.getVenue().getCapacity()
                        : item.getVenue().getCapacity();
        VenueType venueType =
                (item.getVenue().getType() == null)
                        ? existingTicket.getVenue().getType()
                        : item.getVenue().getType();

        Coordinates coordinates = new Coordinates(xCoord, yCoord);
        Venue venue =
                new Venue(existingTicket.getVenue().getId(), venueName, venueCapacity, venueType);
        Ticket newTicket =
                new Ticket(
                        id,
                        ticketName,
                        coordinates,
                        ticketCreationDate,
                        price,
                        comment,
                        refundable,
                        ticketType,
                        venue);

        elements.remove(existingTicket);
        elements.add(newTicket);
    }

    /**
     * This method removes an element from the database by id
     *
     * @param id element id
     * @throws DatabaseException if the specified id does not exist in the database
     */
    @Override
    public void remove(@NonNull Integer id) throws DatabaseException
    {
        Iterator<Ticket> iterator = elements.iterator();

        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();

            if (id.equals(ticket.getId())) {
                iterator.remove();
                return;
            }
        }

        throw new DatabaseException("Element with id " + id + " not found");
    }

    /** This method clears the database */
    @Override
    public void clear()
    {
        elements.clear();
    }

    /**
     * This method saves the collection to a file
     *
     * @throws DatabaseException if something went wrong during the database saving
     */
    @Override
    public void save() throws DatabaseException
    {
	    try
	    {
			fms.validateFileExists(savingLoadingFile);
			fms.validateFileIsWritable(savingLoadingFile);
			savingService.save(savingLoadingFile, this);
	    }
		catch (FileException e)
		{
			throw new DatabaseException("File validation not succeed", e);
		}
    }

    /**
     * This method loads the collection to a file
     *
     * @throws DatabaseException if something went wrong during the database loading
     */
    @Override
    public void load() throws DatabaseException
    {
		try
	    {
			fms.validateFileExists(savingLoadingFile);
			fms.validateFileIsReadable(savingLoadingFile);
			savingService.load(savingLoadingFile, this);
	    }
        catch (FileNotExistsException e)
        {
            throw new DatabaseLoadingFailedException("Failed to load the database", e);
        }
		catch (FileException e)
		{
			throw new DatabaseException("File validation not succeed", e);
		}
    }

    @Override
    public void initEmptyDb()
    {
        this.creationDate = LocalDateTime.now(ZoneId.systemDefault());
        this.elements = new ArrayList<>();
    }

    /**
     * This method removes the first element from the database
     *
     * @throws DatabaseException if required element not found
     */
    @Override
    public void removeFirst() throws DatabaseException
    {
        if (elements.size() == 0)
            throw new DatabaseException("There are no elements in the database. Cannot remove first");

        elements.remove(0);
    }

    /**
     * This method removes the last element from the database
     *
     * @throws DatabaseException if required element not found
     */
    @Override
    public void removeLast() throws DatabaseException
    {
        if (elements.size() == 0)
            throw new DatabaseException("There are no elements in the database. Cannot remove last");

        elements.remove(elements.size() - 1);
    }

    /**
     * This method removes element which is lower than specified one
     *
     * @param item an element for the comparison
     * @throws DatabaseException if required element not found
     */
    @Override
    public void removeLower(@NonNull Ticket item) throws DatabaseException
    {
        List<Integer> removedIds =
                elements.stream()
                        .filter(ticket -> ticket.compareTo(item) < 0)
                        .map(Ticket::getId)
                        .toList();

        for (Integer id : removedIds)
            remove(id);
    }

    /**
     * This method removes all the elements with the refundable status specified
     *
     * @param refundable refundable status of removable elements
     * @throws DatabaseException if it's nothing to remove
     */
    @Override
    public void removeAnyByRefundable(@NonNull Boolean refundable) throws DatabaseException
    {
        List<Integer> ticketsIDsToRemove =
                elements.stream()
                        .filter(
                                ticket ->
                                        ticket.getRefundable().booleanValue()
                                                == refundable.booleanValue())
                        .map(Ticket::getId)
                        .toList();

        if (ticketsIDsToRemove.size() == 0)
            throw new DatabaseException("There are no elements to be removed");

        for (Integer id : ticketsIDsToRemove)
		{
            remove(id);
        }
    }

    /**
     * This method retrieves all the elements with type less than specified
     *
     * @param type type to filter
     * @return list of filtered elements
     */
    @Override
    public List<Ticket> findLessThanType(@NonNull TicketType type)
    {
        return elements.stream()
                .filter(
                        ticket ->
                                (ticket.getType() == null || ticket.getType().compareTo(type) > 0))
                .toList();
    }

	/**
     * This method checks the id existence in the database
     *
     * @param id an id to be checked
     * @return true if id exists
     */
	@Override
	public boolean checkIdExistence(int id)
	{
		return elements.stream().anyMatch((el) -> el.getId().equals(id));
	}
}
