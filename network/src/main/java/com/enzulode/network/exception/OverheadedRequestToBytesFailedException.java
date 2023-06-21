package com.enzulode.network.exception;

/**
 * This exception represents every issue during overheaded requests to bytes conversion
 *
 */
public class OverheadedRequestToBytesFailedException extends NetworkException
{
	/**
	 * OverheadedRequestToBytesFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public OverheadedRequestToBytesFailedException(String message)
	{
		super(message);
	}

	/**
	 * OverheadedRequestToBytesFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public OverheadedRequestToBytesFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
