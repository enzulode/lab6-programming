package com.enzulode.network.exception;

/**
 * This exception represents failed request with overhead
 *
 */
public class FailedRequestWithOverheadException extends RequestFailedException
{
	/**
	 * FailedRequestWithOverheadException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedRequestWithOverheadException(String message)
	{
		super(message);
	}

	/**
	 * FailedRequestWithOverheadException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedRequestWithOverheadException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
