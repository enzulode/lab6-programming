package com.enzulode.network.exception;

/**
 * This exception represents every issue with overheaded responses
 *
 */
public class FailedResponseWithOverheadException extends NetworkException
{
	/**
	 * FailedResponseWithOverheadException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedResponseWithOverheadException(String message)
	{
		super(message);
	}

	/**
	 * FailedResponseWithOverheadException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedResponseWithOverheadException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
