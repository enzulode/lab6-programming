package com.enzulode.network.exception;

/**
 * This exception represents every issue with not overheaded responses
 *
 */
public class FailedResponseWithNoOverheadException extends NetworkException
{
	/**
	 * FailedResponseWithNoOverheadException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedResponseWithNoOverheadException(String message)
	{
		super(message);
	}

	/**
	 * FailedResponseWithNoOverheadException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedResponseWithNoOverheadException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
