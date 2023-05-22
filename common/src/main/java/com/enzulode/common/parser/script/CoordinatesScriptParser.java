package com.enzulode.common.parser.script;

import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.validation.CoordinatesValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Coordinates;

import java.util.Scanner;

/**
 * Class for parsing coordinates from script
 *
 */
public class CoordinatesScriptParser
{
    /**
     * Parse coordinates from strings
     *
     * @param scanner scanner instance
     * @param mode coordinates resolving mode
     * @return coordinates instance
     * @throws ParsingException if something went wrong during parsing dataclass from script
     */
    public static Coordinates parseCoordinates(Scanner scanner, ParserMode mode) throws ParsingException
    {
        try
        {
            Float x = TypesParser.parseFloat(scanner.nextLine());
            Integer y = TypesParser.parseInteger(scanner.nextLine());

            Coordinates coordinates = new Coordinates(x, y);

            if (mode == ParserMode.CREATE) {
                CoordinatesValidator.validateCoordinates(coordinates);
            }

            return coordinates;
        }
		catch (ValidationException e)
		{
            throw new ParsingException("Failed to parse coordinates: validation not succeed", e);
        }
		catch (ParsingException e)
		{
			throw new ParsingException(e.getMessage(), e);
		}
		catch (Exception e)
		{
            throw new ParsingException("Something went wrong during coordinates parsing", e);
        }
    }
}
