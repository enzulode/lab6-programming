package com.enzulode.common.parser.script;

import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.validation.TicketValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Coordinates;
import com.enzulode.models.Ticket;
import com.enzulode.models.Venue;
import com.enzulode.models.util.TicketType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

/**
 * Class for parsing ticket from script
 *
 */
public class TicketScriptParser
{
    /**
     * Parse ticket from script
     *
     * @param scanner script scanner for ticket scanning
     * @param mode ticket resolving mode
     * @return ticket instance
     * @throws ParsingException if something went wrong during parsing dataclass from script
     */
    public static Ticket parseTicket(Scanner scanner, ParserMode mode) throws ParsingException
    {
        try
        {
            String name = TypesParser.parseString(scanner.nextLine());
            Coordinates coordinates = CoordinatesScriptParser.parseCoordinates(scanner, mode);
            Float price = TypesParser.parseFloat(scanner.nextLine());
            String comment = TypesParser.parseString(scanner.nextLine());
            Boolean refundable = TypesParser.parseBoolean(scanner.nextLine());
            TicketType ticketType = TypesParser.parseTicketType(scanner.nextLine());
            Venue venue = VenueScriptParser.parseVenue(scanner, mode);

            Ticket ticket =
                    new Ticket(
                            name,
                            coordinates,
                            LocalDateTime.now(ZoneId.systemDefault()),
                            price,
                            comment,
                            refundable,
                            ticketType,
                            venue);

            if (mode == ParserMode.CREATE) TicketValidator.validateTicket(ticket);

            return ticket;
        }
		catch (ValidationException | ParsingException e)
		{
            throw new ParsingException(e.getMessage(), e);
        }
		catch (Exception e)
		{
            throw new ParsingException("Something went wrong during ticket parsing", e);
        }
    }
}