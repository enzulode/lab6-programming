package com.enzulode.network.exception;

/**
 * This exception represents a situation when the server is not accessible
 *
 */
public class ServerNotAvailableException extends NetworkException
{
	/**
	 * ServerNotAvailableException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public ServerNotAvailableException(String message)
	{
		super(message);
	}

	/**
	 * ServerNotAvailableException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public ServerNotAvailableException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
