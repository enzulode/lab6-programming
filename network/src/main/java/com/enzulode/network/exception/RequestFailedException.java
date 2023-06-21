package com.enzulode.network.exception;

/**
 * This exception represents every request sending failed
 *
 */
public class RequestFailedException extends NetworkException
{
	/**
	 * RequestFailed constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public RequestFailedException(String message)
	{
		super(message);
	}

	/**
	 * RequestFailed constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public RequestFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
