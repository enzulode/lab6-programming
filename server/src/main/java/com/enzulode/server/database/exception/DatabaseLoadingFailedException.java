package com.enzulode.server.database.exception;

/**
 * Database loading failed situation is represented by this exception
 *
 */
public class DatabaseLoadingFailedException extends DatabaseException
{
    /**
     * DatabaseLoadingFailedException constructor without cause
     *
     * @param message exception message
     */
    public DatabaseLoadingFailedException(String message)
    {
        super(message);
    }

    /**
     * DatabaseLoadingFailedException constructor
     *
     * @param message exception message
     * @param cause exception cause
     */
    public DatabaseLoadingFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
