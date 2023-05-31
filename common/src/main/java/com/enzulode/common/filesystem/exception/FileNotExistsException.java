package com.enzulode.common.filesystem.exception;

/**
 * This exception means that the requested file does not exist
 *
 */
public class FileNotExistsException extends FileException
{
    /**
     * FileNotExistsException constructor without cause
     *
     * @param message exception message
     */
    public FileNotExistsException(String message)
    {
        super(message);
    }

    /**
     * FileNotExistsException constructor
     *
     * @param message exception message
     * @param cause exception cause
     */
    public FileNotExistsException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
