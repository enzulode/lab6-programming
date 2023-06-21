package com.enzulode.network.exception;

/**
 * This exception represents every issue during request procedure from frames into frame instance
 *
 */
public class RequestFromFramesProcedureFailedException extends NetworkException
{
	/**
	 * RequestFromFramesProcedureFailedException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public RequestFromFramesProcedureFailedException(String message)
	{
		super(message);
	}

	/**
	 * RequestFromFramesProcedureFailedException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public RequestFromFramesProcedureFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
