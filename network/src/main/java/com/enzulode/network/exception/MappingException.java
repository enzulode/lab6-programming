package com.enzulode.network.exception;

/**
 * This exception represents every issue during mapping process
 *
 */
public class MappingException extends NetworkException
{
	/**
	 * MappingException constructor with no cause provided
	 *
	 * @param message exception message
	 */
	public MappingException(String message)
	{
		super(message);
	}

	/**
	 * MappingException constructor
	 *
	 * @param message exception message
	 * @param cause   exception cause
	 */
	public MappingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
