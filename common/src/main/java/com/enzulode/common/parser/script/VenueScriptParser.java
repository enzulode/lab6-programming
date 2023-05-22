package com.enzulode.common.parser.script;

import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.validation.VenueValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Venue;
import com.enzulode.models.util.VenueType;

import java.util.Scanner;

/**
 * Class for parsing venue from script
 *
 */
public class VenueScriptParser
{
    /**
     * Parse venue from strings
     *
     * @param scanner script scanner
     * @param mode venue resolving mode
     * @return venue instance
     * @throws ParsingException if something went wrong during parsing dataclass from script
     */
    public static Venue parseVenue(Scanner scanner, ParserMode mode) throws ParsingException
    {
        try
        {
            String name = TypesParser.parseString(scanner.nextLine());
            Integer capacity = TypesParser.parseInteger(scanner.nextLine());
            VenueType type = TypesParser.parseVenueType(scanner.nextLine());

            Venue venue = new Venue(name, capacity, type);

            if (mode == ParserMode.CREATE) VenueValidator.validateVenue(venue);

            return venue;
        }
		catch (ValidationException | ParsingException e)
		{
            throw new ParsingException(e.getMessage(), e);
        }
		catch (Exception e)
		{
            throw new ParsingException("Something went wrong during venue parsing", e);
        }
    }
}
