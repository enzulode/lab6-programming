package com.enzulode.common.resolution.exception;

public class CommandResolutionException extends Exception
{
	public CommandResolutionException(String message)
	{
		super(message, null, false, false);
	}

	public CommandResolutionException(String message, Throwable cause)
	{
		super(message, cause, false, false);
	}
}
