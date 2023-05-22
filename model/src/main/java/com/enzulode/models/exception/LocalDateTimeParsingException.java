package com.enzulode.models.exception;

public class LocalDateTimeParsingException extends Exception
{
	public LocalDateTimeParsingException(String message)
	{
		super(message);
	}

	public LocalDateTimeParsingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
