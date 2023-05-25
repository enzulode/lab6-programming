package com.enzulode.common.filesystem;

import com.enzulode.common.filesystem.exception.FileException;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/** The service for using files, retrieving them by name and other stuff */
public class FileManipulationService
{
    /**
     * This method validates file to be writable
     *
     * @param file file for validation
     * @throws FileException if file is not writable
     */
    public void validateFileIsWritable(@NonNull File file) throws FileException
    {
        if (!Files.isWritable(file.toPath()))
            throw new FileException("Specified file is not writable");
    }

    /**
     * This method validates file to be writable
     *
     * @param file file for validation
     * @throws FileException if file is not readable
     */
    public void validateFileIsReadable(@NonNull File file) throws FileException
    {
        if (!Files.isReadable(file.toPath()))
            throw new FileException("Specified file is not readable");
    }

    /**
     * This method validates file to exist
     *
     * @param file file for validation
     * @throws FileException if file does not exist
     */
    public void validateFileExists(@NonNull File file) throws FileException
    {
        if (!file.exists()) throw new FileException("Specified file does not exists");
    }

    /**
     * Method retrieves file for specified path
     *
     * @param filePath requested file path
     * @return file object
     * @throws FileException if specified file is not writable, readable or not exists
     */
    public File getFileByName(@NonNull String filePath) throws FileException
    {
        File file = new File(filePath);

        validateFileExists(file);
        validateFileIsReadable(file);
        validateFileIsWritable(file);

        return file;
    }

    /**
     * This method reads all file lines and returns them as a list
     *
     * @param file file to fetch its lines
     * @return a list of file lines
     * @throws FileException if file doesn't have enough permissions to read or even doesn't exist
     */
    public List<String> readFileLines(@NonNull File file) throws FileException
    {
//      Check if file exists, and we are able to read from it
        validateFileExists(file);
        validateFileIsReadable(file);

        try(BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            return reader.lines().toList();
        }
        catch (IOException e)
        {
            throw new FileException("Failed to open file", e);
        }
    }

    /**
     * This method creates a BufferedReader for a file by name
     *
     * @param filename the name of the file
     * @return the reader instance
     * @throws FileException if file doesn't have enough permissions to read or even doesn't exist
     */
    public BufferedReader getReaderByName(@NonNull String filename) throws FileException
    {
        File file = getFileByName(filename);

//      Check is file ready to read
        validateFileExists(file);
        validateFileIsReadable(file);

        try
        {
            return new BufferedReader(new FileReader(file));
        }
        catch (IOException e)
        {
            throw new FileException("Failed to create file reader", e);
        }
    }
}
