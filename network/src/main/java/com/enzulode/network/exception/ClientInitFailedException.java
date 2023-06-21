package com.enzulode.network.exception;

/**
 * This exception represents every issue during client init
 *
 */
public class ClientInitFailedException extends NetworkException
{
	/**
	 * ClientInitFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public ClientInitFailedException(String message)
	{
		super(message);
	}

	/**
	 * ClientInitFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public ClientInitFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
