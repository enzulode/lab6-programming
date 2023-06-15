package com.enzulode.common.dao;

import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.models.Ticket;
import com.enzulode.models.util.TicketType;
import lombok.NonNull;

import java.util.List;

/**
 * An abstraction for application data access object
 *
 */
public interface TicketDao extends CrudDAO<Ticket>
{
	/**
	 * This is an abstraction for method that removes the first element from the database
	 *
	 * @throws DaoException if required element not found
	 */
	void removeFirst() throws DaoException;

	/**
	 * This is an abstraction for method that removes the last element from the database
	 *
	 * @throws DaoException if required element not found
	 */
	void removeLast() throws DaoException;

	/**
	 * This is an abstraction for method that removes element which is lower than specified one
	 *
	 * @param object an element for the comparison
	 * @throws DaoException if required element not found
	 */
	void removeLower(@NonNull Ticket object) throws DaoException;

	/**
	 * This is an abstraction for method that removes all the elements with the refundable status
	 * specified
	 *
	 * @param refundable refundable status of removable elements
	 * @throws DaoException if it's nothing to remove
	 */
	void removeAnyByRefundable(@NonNull Boolean refundable) throws DaoException;

	/**
	 * This is an abstraction for method that retrieves all the elements with type less than
	 * specified
	 *
	 * @param type type to filter
	 * @return list of filtered elements
	 */
	List<Ticket> findLessThanType(@NonNull TicketType type);

	/**
	 * This method checks the id existence in the database
	 *
	 * @param id an id to be checked
	 * @return true if id exists
	 */
	boolean checkIdExistence(@NonNull Integer id);
}
