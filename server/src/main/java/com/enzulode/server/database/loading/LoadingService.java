package com.enzulode.server.database.loading;

import com.enzulode.server.database.AbstractDatabase;
import com.enzulode.server.database.loading.exception.LoadingException;

/**
 * This interface declares a loading contract
 *
 * @param <T> database stored type
 */
public interface LoadingService<T>
{
	/**
	 * This method loads the database from a specific source
	 *
	 * @param database the database instance
	 * @throws LoadingException if something went wrong during loading
	 */
	void load(AbstractDatabase<T> database) throws LoadingException;
}
