package com.enzulode.network.exception;

/**
 * This exception represents the situation when the client close failed
 *
 */
public class FailedToCloseClientException extends NetworkException
{
	/**
	 * FailedToCloseClientException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedToCloseClientException(String message)
	{
		super(message);
	}

	/**
	 * FailedToCloseClientException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedToCloseClientException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
