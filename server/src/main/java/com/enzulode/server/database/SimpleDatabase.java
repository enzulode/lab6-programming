package com.enzulode.server.database;

import com.enzulode.server.database.exception.DatabaseException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface represents a simple database with basic data-manipulation and data-definition methods
 *
 * @param <T> operating data types
 */
public interface SimpleDatabase<T>
{
	/**
	 * This method inits default database ddl
	 *
	 * @throws DatabaseException if database ddl initiation failed due to networking issues
	 */
	void initDDLIfNotExists() throws DatabaseException;

	/**
	 * This method retrieves current database size
	 *
	 * @return current database size
	 */
	int size() throws DatabaseException;

	/**
	 * This method retrieves database creation date
	 *
	 * @return database creation date
	 */
	LocalDateTime getCreationDate() throws DatabaseException;

	/**
	 * This method clears the database
	 *
	 * @throws DatabaseException if database clearing failed
	 */
	void clear() throws DatabaseException;

	/**
	 * This method retrieves all stored tickets from the database
	 *
	 * @return a list of stored elements
	 */
	List<T> findAll();
}
