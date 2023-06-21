package com.enzulode.network.exception;

/**
 * This exception represents every issue during frames to bytes conversion
 *
 */
public class FramesToBytesFailedException extends NetworkException
{
	/**
	 * FramesToBytesFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public FramesToBytesFailedException(String message)
	{
		super(message);
	}

	/**
	 * FramesToBytesFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public FramesToBytesFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
