package com.enzulode.server.database;

import com.enzulode.models.Ticket;
import com.enzulode.models.util.TicketType;
import com.enzulode.server.database.exception.DatabaseException;
import lombok.NonNull;

import java.util.List;

/**
 * This interface declares default ticket database functionality
 *
 */
public interface TicketDatabase extends CrudDatabase<Ticket>
{
	/**
	 * This method deletes first stored element
	 *
	 * @throws DatabaseException if deletion failed
	 */
	void removeFirst() throws DatabaseException;

	/**
	 * This method deletes last stored element
	 *
	 * @throws DatabaseException if ticket deletion failed
	 */
	void removeLast() throws DatabaseException;

	/**
	 * This method deletes from the database all tickets lower that provided one
	 *
	 * @param object ticket for comparison
	 * @throws DatabaseException if tickets deletion failed
	 */
	void removeLower(@NonNull Ticket object) throws DatabaseException;

	/**
	 * This method deletes first any ticket with provided refundable status
	 *
	 * @param refundable refundable status
	 * @throws DatabaseException if deletion failed
	 */
	void removeAnyByRefundable(@NonNull Boolean refundable) throws DatabaseException;

	/**
	 * This method retrieves a list of tickets with type less than provided
	 *
	 * @param type ticket type
	 * @return a list of tickets with type less than provided one
	 */
	List<Ticket> findLessThanType(@NonNull TicketType type);

	/**
	 * This method checks id presence in the database
	 *
	 * @param id an id to be checked
	 * @return true if id is present and false if is not
	 */
	boolean checkIdExistence(@NonNull Integer id);
}
