package com.enzulode.server.database;

import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.filesystem.exception.FileException;
import com.enzulode.common.validation.TicketValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Ticket;
import com.enzulode.server.database.exception.DatabaseException;
import lombok.AllArgsConstructor;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ticket database saving service saves and loads the database into / from the file
 *
 */
@AllArgsConstructor
public class TicketDatabaseSavingService
{
    /** FileManipulationService instance */
    private final FileManipulationService fms;

    /** JAXB marshaller instance */
    private final Marshaller xmlMarshaller;

    /** JAXB unmarshaller instance */
    private final Unmarshaller xmlUnmarshaller;

    /**
     * Database saving method
     *
     * @param savingFile database saving file
     * @param database ticket database wrapper instance
     * @throws FileException if something went wrong during file operations
     * @throws DatabaseException if something went wrong during database operations
     */
    public void save(File savingFile, Database<Ticket> database) throws FileException, DatabaseException
    {
        fms.validateFileExists(savingFile);
        fms.validateFileIsWritable(savingFile);

        try
        {
            FileOutputStream fos = new FileOutputStream(savingFile);
            xmlMarshaller.marshal(database, fos);
        }
        catch (FileNotFoundException e)
        {
            throw new DatabaseException("Database saving file does not exists", e);
        }
        catch (JAXBException e)
        {
            throw new DatabaseException("Unexpected error occurred during saving the database", e);
        }
    }

    /**
     * Database loading method
     *
     * @throws FileException if something went wrong during file operations
     * @throws DatabaseException if something went wrong during database operations
     */
    @SuppressWarnings("unchecked")
    public void load(File loadingFile, Database<Ticket> database) throws FileException, DatabaseException
    {
        fms.validateFileExists(loadingFile);
        fms.validateFileIsWritable(loadingFile);

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(loadingFile));
            Database<Ticket> buffer = (Database<Ticket>) xmlUnmarshaller.unmarshal(reader);
//            TicketDatabaseImpl buffer = (TicketDatabaseImpl) xmlUnmarshaller.unmarshal(reader);

            if (buffer.getCreationDate() == null)
                throw new DatabaseException("Unable to load the database: creation date does not exist");


            Set<Integer> usedTicketIds = new HashSet<>();
            Set<Long> usedVenueIds = new HashSet<>();
            List<Ticket> elements = buffer.findAll();
            List<Ticket> correctElements = new ArrayList<>();

            for (Ticket element : elements)
            {
                if (element.getId() == null || usedTicketIds.contains(element.getId()))
                {
                    throw new DatabaseException("Unable to load the database. Tickets should have unique ids");
                }

                if (element.getVenue() == null || usedVenueIds.contains(element.getVenue().getId()))
                {
                    throw new DatabaseException("Unable to load the database. Venues should have unique ids");
                }

                if (element.getVenue().getId() == 0)
                {
                    throw new DatabaseException("Unable to load venue id");
                }

                try
                {
                    TicketValidator.validateTicket(element);
                    usedTicketIds.add(element.getId());
                    usedVenueIds.add(element.getVenue().getId());
                    correctElements.add(element);
                }
                catch (ValidationException e)
                {
                    throw new DatabaseException("Unable to load the database. One of stored elements is corrupted");
                }
            }

            database.setElements(correctElements);
            database.setCreationDate(buffer.getCreationDate());
        }
        catch (FileNotFoundException e)
        {
            throw new FileException("Specified loading file does not exist");
        }
        catch (JAXBException e)
        {
            throw new DatabaseException("Unexpected error occurred. Unable to load the database");
        }
    }
}
