package com.enzulode.server.database.loading.exception;

/**
 * This exception represents loading issues
 *
 */
public class LoadingException extends Exception
{
	/**
	 * Loading exception constructor without a cause
	 *
	 * @param message exception message
	 */
	public LoadingException(String message)
	{
		super(message);
	}

	/**
	 * Loading exception constructor
	 *
	 * @param message exception message
	 * @param cause exception cause
	 */
	public LoadingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
