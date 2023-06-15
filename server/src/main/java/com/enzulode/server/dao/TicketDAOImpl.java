package com.enzulode.server.dao;

import com.enzulode.common.dao.TicketDao;
import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.models.Ticket;
import com.enzulode.models.util.TicketType;
import com.enzulode.server.database.TicketDatabase;
import com.enzulode.server.database.exception.DatabaseException;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Ticket DAO implementation
 *
 */
public class TicketDAOImpl implements TicketDao
{
	/**
	 * Ticket database instance
	 *
	 */
	private final TicketDatabase database;

	/**
	 * Ticket dao implementation constructor
	 *
	 * @param database ticket database instance
	 */
	public TicketDAOImpl(@NonNull TicketDatabase database)
	{
		this.database = database;
	}

	/**
	 * This method inserts an object into the database
	 *
	 * @param object object to be inserted
	 * @throws DaoException if insertion failed
	 */
	@Override
	public void create(@NonNull Ticket object) throws DaoException
	{
		try
		{
			database.create(object);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to create an object in the database", e);
		}
	}

	/**
	 * This method retrieves an object by id
	 *
	 * @param id object id
	 * @return an optional object value
	 * @throws DaoException if read operation failed
	 */
	@Override
	public Optional<Ticket> read(@NonNull Integer id) throws DaoException
	{
		try
		{
			return database.read(id);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to read an object by id from the database", e);
		}
	}

	/**
	 * This method updates an already stored object
	 *
	 * @param id     updatable object id
	 * @param object object to be updated
	 * @throws DaoException if update failed
	 */
	@Override
	public void update(@NonNull Integer id, @NonNull Ticket object) throws DaoException
	{
		try
		{
			database.update(id, object);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to update an object by id", e);
		}
	}

	/**
	 * This method deletes an existing object
	 *
	 * @param id deleted object id
	 * @throws DaoException if deletion failed
	 */
	@Override
	public void delete(@NonNull Integer id) throws DaoException
	{
		try
		{
			database.delete(id);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to delete an object from the database", e);
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
			throw new DaoException("Failed to delete first element from the database", e);
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
			throw new DaoException("Failed to remove last object from the database", e);
		}
	}

	/**
	 * This is an abstraction for method that removes element which is lower than specified one
	 *
	 * @param object an element for the comparison
	 * @throws DaoException if required element not found
	 */
	@Override
	public void removeLower(@NonNull Ticket object) throws DaoException
	{
		try
		{
			database.removeLower(object);
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to remove lower element from the database", e);
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
			throw new DaoException("Failed to remove object by refundable status from the database", e);
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
	public boolean checkIdExistence(@NonNull Integer id)
	{
		return database.checkIdExistence(id);
	}

	/**
	 * This method retrieves current database size
	 *
	 * @return current database size
	 * @throws DaoException if size retrieving failed
	 */
	@Override
	public int size() throws DaoException
	{
		try
		{
			return database.size();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to retrieve database size", e);
		}
	}

	/**
	 * This method retrieves database creation date
	 *
	 * @return database creation date
	 * @throws DaoException if creation date retrieving failed
	 */
	@Override
	public LocalDateTime getCreationDate() throws DaoException
	{
		try
		{
			return database.getCreationDate();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to retrieve database creation date", e);
		}
	}

	/**
	 * This method clears the database
	 *
	 * @throws DaoException if database clearing failed
	 */
	@Override
	public void clear() throws DaoException
	{
		try
		{
			database.clear();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to clear the database", e);
		}
	}

	/**
	 * This method retrieves all stored tickets from the database
	 *
	 * @return a list of stored elements
	 */
	@Override
	public List<Ticket> findAll() throws DaoException
	{
		try
		{
			return database.findAll();
		}
		catch (DatabaseException e)
		{
			throw new DaoException("Failed to retrieve objects from the database", e);
		}
	}
}
