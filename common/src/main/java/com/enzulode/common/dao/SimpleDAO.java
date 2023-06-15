package com.enzulode.common.dao;

import com.enzulode.common.dao.exception.DaoException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple DAO interface
 *
 * @param <T> operating data types
 */
public interface SimpleDAO<T>
{
	/**
	 * This method retrieves current database size
	 *
	 * @return current database size
	 * @throws DaoException if size retrieving failed
	 */
	int size() throws DaoException;

	/**
	 * This method retrieves database creation date
	 *
	 * @return database creation date
	 * @throws DaoException if creation date retrieving failed
	 */
	LocalDateTime getCreationDate() throws DaoException;

	/**
	 * This method clears the database
	 *
	 * @throws DaoException if database clearing failed
	 */
	void clear() throws DaoException;

	/**
	 * This method retrieves all stored tickets from the database
	 *
	 * @return a list of stored elements
	 */
	List<T> findAll() throws DaoException;
}
