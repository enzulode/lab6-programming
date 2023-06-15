package com.enzulode.server.dao;

import com.enzulode.common.dao.Dao;
import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.models.Ticket;
import com.enzulode.models.util.TicketType;
import com.enzulode.server.database.Database;
import com.enzulode.server.database.exception.DatabaseException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Ticket dao implementation
 *
 */
@RequiredArgsConstructor
public class TicketDaoImpl implements Dao<Ticket>
{
	/**
	 * Database instance
	 *
	 */
	private final Database<Ticket> database;

	/**
	 * This is an abstraction for method retrieves amount of stored elements
	 *
	 * @return amount of stored elements
	 */
	@Override
	public int size()
	{
		return database.size();
	}

	/**
	 * This is an abstraction for method retrieves creation date of the database
	 *
	 * @return database creation date
	 */
	@Override
	public LocalDateTime getCreationDate()
	{
		return database.getCreationDate();
	}

	/**
	 * This is an abstraction for method that retrieves all elements from the database
	 *
	 * @return list of stored elements
	 */
	@Override
	public List<Ticket> findAll()
	{
		return database.findAll();
	}

	/**
	 * This is an abstraction for method that is retrieving stored element in the database by id
	 *
	 * @param id stored element id
	 * @return element
	 * @throws DaoException if the specified id does not exist in the database
	 */
	@Override
	public Ticket findElementById(@NonNull Integer id) throws DaoException
	{
		try
		{
			return database.findElementById(id);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to find an element. Database layer threw an exception", e);
		}
	}

	/**
	 * This is an abstraction for method that provides an ability to add new element into the
	 * database
	 *
	 * @param item the new element which should be added into the database
	 * @throws DaoException if trying to add new element with existing id
	 */
	@Override
	public void add(@NonNull Ticket item) throws DaoException
	{
		try
		{
			database.add(item);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to add new element into the database", e);
		}
	}

	/**
	 * This is an abstraction for method that provides an ability to update the existing element by
	 * id
	 *
	 * @param id   existing element id
	 * @param item element that the existing element will be updated with
	 * @throws DaoException if the specified id does not exist in the database
	 */
	@Override
	public void update(@NonNull Integer id, @NonNull Ticket item) throws DaoException
	{
		try
		{
			database.update(id, item);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to update an element. Database layer threw an exception", e);
		}
	}

	/**
	 * This is an abstraction for method that removes an element from the database by id
	 *
	 * @param id element id
	 * @throws DaoException if the specified id does not exist in the database
	 */
	@Override
	public void remove(@NonNull Integer id) throws DaoException
	{
		try
		{
			database.remove(id);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to remove an element from the database", e);
		}
	}

	/**
	 * This is an abstraction for method that clears the database
	 */
	@Override
	public void clear()
	{
		database.clear();
	}

	/**
	 * This is an abstraction for method that saves the collection to a file
	 *
	 * @throws DaoException if something went wrong during the database saving
	 */
	@Override
	public void save() throws DaoException
	{
		try
		{
			database.save();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to save the database", e);
		}
	}

	/**
	 * This is an abstraction for method that removes the first element from the database
	 *
	 * @throws DaoException if required element not found
	 */
	@Override
	public void removeFirst() throws DaoException
	{
		try
		{
			database.removeFirst();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("First element cannot be removed: does not exist", e);
		}
	}

	/**
	 * This is an abstraction for method that removes the last element from the database
	 *
	 * @throws DaoException if required element not found
	 */
	@Override
	public void removeLast() throws DaoException
	{
		try
		{
			database.removeLast();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to remove the last element: does not exist", e);
		}
	}

	/**
	 * This is an abstraction for method that removes element which is lower than specified one
	 *
	 * @param item an element for the comparison
	 * @throws DaoException if required element not found
	 */
	@Override
	public void removeLower(@NonNull Ticket item) throws DaoException
	{
		try
		{
			database.removeLower(item);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to remove the lower element: does not exist", e);
		}
	}

	/**
	 * This is an abstraction for method that removes all the elements with the refundable status
	 * specified
	 *
	 * @param refundable refundable status of removable elements
	 * @throws DaoException if it's nothing to remove
	 */
	@Override
	public void removeAnyByRefundable(@NonNull Boolean refundable) throws DaoException
	{
		try
		{
			database.removeAnyByRefundable(refundable);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to remove element by refundable status: not found", e);
		}
	}

	/**
	 * This is an abstraction for method that retrieves all the elements with type less than
	 * specified
	 *
	 * @param type type to filter
	 * @return list of filtered elements
	 */
	@Override
	public List<Ticket> findLessThanType(@NonNull TicketType type)
	{
		return database.findLessThanType(type);
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
		return database.checkIdExistence(id);
	}
}
