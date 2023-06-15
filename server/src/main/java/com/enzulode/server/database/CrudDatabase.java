package com.enzulode.server.database;

import com.enzulode.server.database.exception.DatabaseException;
import lombok.NonNull;

import java.util.Optional;

/**
 * This interface represents the database with simple set of CRUD operations
 *
 * @param <T> operating data types
 */
public interface CrudDatabase<T> extends SimpleDatabase<T>
{
	/**
	 * This method inserts an object into the database
	 *
	 * @param object object to be inserted
	 * @throws DatabaseException if insertion failed
	 */
	void create(@NonNull T object) throws DatabaseException;

	/**
	 * This method retrieves an object by id
	 *
	 * @param id object id
	 * @return an optional object value
	 */
	Optional<T> read(@NonNull Integer id) throws DatabaseException;

	/**
	 * This method updates an already stored object
	 *
	 * @param id updatable object id
	 * @param object object to be updated
	 * @throws DatabaseException if update failed
	 */
	void update(@NonNull Integer id, @NonNull T object) throws DatabaseException;

	/**
	 * This method deletes an existing object
	 *
	 * @param id deleted object id
	 * @throws DatabaseException if deletion failed
	 */
	void delete(@NonNull Integer id) throws DatabaseException;
}
