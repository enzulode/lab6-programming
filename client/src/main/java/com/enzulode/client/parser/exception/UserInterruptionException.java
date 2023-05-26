package com.enzulode.client.parser.exception;

public class UserInterruptionException extends Exception
{
	public UserInterruptionException(String message)
	{
		super(message, null, false, false);
	}

	public UserInterruptionException(String message, Throwable cause)
	{
		super(message, cause, false, false);
	}
}
