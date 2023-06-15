package com.enzulode.common.dao;

import com.enzulode.common.dao.exception.DaoException;
import lombok.NonNull;

import java.util.Optional;

/**
 * CRUD DAO interface
 *
 * @param <T> operating data types
 */
public interface CrudDAO<T> extends SimpleDAO<T>
{
	/**
	 * This method inserts an object into the database
	 *
	 * @param object object to be inserted
	 * @throws DaoException if insertion failed
	 */
	void create(@NonNull T object) throws DaoException;

	/**
	 * This method retrieves an object by id
	 *
	 * @param id object id
	 * @return an optional object value
	 * @throws DaoException if read operation failed
	 */
	Optional<T> read(@NonNull Integer id) throws DaoException;

	/**
	 * This method updates an already stored object
	 *
	 * @param id updatable object id
	 * @param object object to be updated
	 * @throws DaoException if update failed
	 */
	void update(@NonNull Integer id, @NonNull T object) throws DaoException;

	/**
	 * This method deletes an existing object
	 *
	 * @param id deleted object id
	 * @throws DaoException if deletion failed
	 */
	void delete(@NonNull Integer id) throws DaoException;
}
