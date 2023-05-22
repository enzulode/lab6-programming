package com.enzulode.common.filesystem;

import com.enzulode.common.filesystem.exception.FileException;

import java.io.File;
import java.nio.file.Files;

/** The service for using files, retrieving them by name and other stuff */
public class FileManipulationService {
    /**
     * This method validates file to be writable
     *
     * @param file file for validation
     * @throws FileException if file is not writable
     */
    public void validateFileIsWritable(File file) throws FileException
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
    public void validateFileIsReadable(File file) throws FileException {
        if (!Files.isReadable(file.toPath()))
            throw new FileException("Specified file is not readable");
    }

    /**
     * This method validates file to exist
     *
     * @param file file for validation
     * @throws FileException if file does not exist
     */
    public void validateFileExists(File file) throws FileException {
        if (!file.exists()) throw new FileException("Specified file does not exists");
    }

    /**
     * Method retrieves file for specified path
     *
     * @param filePath requested file path
     * @return file object
     * @throws FileException if specified file is not writable, readable or not exists
     */
    public File getFileByName(String filePath) throws FileException {
        File file = new File(filePath);

        validateFileExists(file);
        validateFileIsReadable(file);
        validateFileIsWritable(file);

        return file;
    }
}
