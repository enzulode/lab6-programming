package com.enzulode.network.exception;

/**
 * This exception represents failed request with no overhead
 *
 */
public class FailedRequestNoOverheadException extends RequestFailedException
{
	/**
	 * FailedRequestNoOverheadException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedRequestNoOverheadException(String message)
	{
		super(message);
	}

	/**
	 * FailedRequestNoOverheadException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedRequestNoOverheadException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
