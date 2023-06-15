package com.enzulode.server.database.connection;

import com.enzulode.server.database.connection.exception.DatabaseConnectionException;

import java.sql.Connection;

/**
 * Database connection manager abstraction
 *
 */
public interface DatabaseConnectionManager extends AutoCloseable
{
	/**
	 * This method retrieves a connection for the database
	 *
	 * @return connection instance
	 * @throws DatabaseConnectionException if it's failed to retrieve a connection
	 */
	Connection getConnection() throws DatabaseConnectionException;

	/**
	 * This method closes a connection manager
	 *
	 */
	void close();
}
