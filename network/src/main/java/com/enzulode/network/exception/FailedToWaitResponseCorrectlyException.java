package com.enzulode.network.exception;

/**
 * This exception represents every issue during response waiting
 *
 */
public class FailedToWaitResponseCorrectlyException extends NetworkException
{
	/**
	 * FailedToWaitResponseCorrectlyException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedToWaitResponseCorrectlyException(String message)
	{
		super(message);
	}

	/**
	 * FailedToWaitResponseCorrectlyException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedToWaitResponseCorrectlyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
