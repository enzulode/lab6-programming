package com.enzulode.network.exception;

/**
 * This exception represents every issue during service initialization
 *
 */
public class ServerInitializationFailedException extends NetworkException
{
	/**
	 * ServerInitializationFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public ServerInitializationFailedException(String message)
	{
		super(message);
	}

	/**
	 * ServerInitializationFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public ServerInitializationFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
