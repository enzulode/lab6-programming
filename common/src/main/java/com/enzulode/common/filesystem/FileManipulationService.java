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
}
