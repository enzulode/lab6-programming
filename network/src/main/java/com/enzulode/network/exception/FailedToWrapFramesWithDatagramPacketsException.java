package com.enzulode.network.exception;

/**
 * This exception represents the situation when it's failed to wrap frames with DatagramPackets
 *
 */
public class FailedToWrapFramesWithDatagramPacketsException extends NetworkException
{
	/**
	 * FailedToWrapFramesWithDatagramPacketsException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FailedToWrapFramesWithDatagramPacketsException(String message)
	{
		super(message);
	}

	/**
	 * FailedToWrapFramesWithDatagramPacketsException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FailedToWrapFramesWithDatagramPacketsException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
