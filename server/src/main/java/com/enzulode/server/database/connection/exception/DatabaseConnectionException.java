package com.enzulode.server.database.connection.exception;

import com.enzulode.server.database.exception.DatabaseException;

/**
 * This exception stand for database connection issues
 *
 */
public class DatabaseConnectionException extends DatabaseException
{
	/**
	 * Database connection exception constructor without cause
	 *
	 * @param message exception message
	 */
	public DatabaseConnectionException(String message)
	{
		super(message);
	}

	/**
	 * Database connection exception constructor
	 *
	 * @param message exception message
	 * @param cause exception cause
	 */
	public DatabaseConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
