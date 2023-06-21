package com.enzulode.network.exception;

/**
 * This exception represents every issue during request handling process
 *
 */
public class RequestListeningFailedException extends NetworkException
{
	/**
	 * RequestHandlingFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public RequestListeningFailedException(String message)
	{
		super(message);
	}

	/**
	 * RequestHandlingFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public RequestListeningFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
