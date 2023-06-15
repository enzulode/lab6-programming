package com.enzulode.server.database.properties.exception;

/**
 * This exception represents issues with the database properties fetching
 *
 */
public class DatabasePropertyException extends Exception
{
	/**
	 * Database property exception constructor without cause
	 *
	 * @param message exception message
	 */
	public DatabasePropertyException(String message)
	{
		super(message);
	}

	/**
	 * Database property exception constructor
	 *
	 * @param message exception message
	 * @param cause exception cause
	 */
	public DatabasePropertyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
