package com.enzulode.client.parser.cli;

import com.enzulode.common.parser.ParserMode;
import com.enzulode.client.parser.exception.UserInterruptionException;
import com.enzulode.client.util.Printer;
import com.enzulode.common.validation.TicketValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Coordinates;
import com.enzulode.models.Ticket;
import com.enzulode.models.Venue;
import com.enzulode.models.util.TicketType;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Provides an ability to parse ticket object from console
 *
 */
@RequiredArgsConstructor
public class TicketCLIParser
{
    /**
     * Printer instance
     *
     */
    private final Printer printer;

    /**
     * Scanner instance
     *
     */
    private final Scanner scanner;

    /**
     * CoordinatesCLIParser instance
     *
     */
    private final CoordinatesCLIParser coordinatesCLIParser;

    /**
     * VenueCLIParser instance
     *
     */
    private final VenueCLIParser venueCLIParser;

    /**
     * Parser resolving mode
     *
     */
    @Setter
    private ParserMode mode;

    /**
     * Offers an ability to interrupt data input
     *
     * @throws UserInterruptionException if got not y/Y from user
     */
    private void proposeContinue() throws UserInterruptionException
    {
        printer.print("Do you want to continue? [y/Y - for yes, other - for no]: ");
        String userInput = scanner.nextLine().trim();
        if (!"Y".equalsIgnoreCase(userInput))
            throw new UserInterruptionException("Data input successfully interrupted");
    }

    /**
     * Method can parse ticket name
     *
     * @return ticket name
     * @throws UserInterruptionException if got not y/Y from user
     */
    private String parseTicketName() throws UserInterruptionException {
        printer.print("Enter ticket name (non-empty): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput))
		{
            if (mode == ParserMode.CREATE)
			{
                printer.println(
                        "You're not able to insert a null value for this variable. Try another value");
                proposeContinue();
                return parseTicketName();
            }
			else
			{
                return null;
            }
        }

        try
        {
            TicketValidator.validateTicketName(userInput);
            return userInput;
        }
		catch (ValidationException e)
		{
            printer.println(e.getMessage());
            proposeContinue();
            return parseTicketName();
        }
    }

    /**
     * Method can parse Coordinates
     *
     * @return coordinates
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Coordinates parseTicketCoordinates() throws UserInterruptionException
    {
        coordinatesCLIParser.setMode(mode);
        return coordinatesCLIParser.parseCoordinates();
    }

    /**
     * Method can parse ticket price
     *
     * @return ticket price
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Float parseTicketPrice() throws UserInterruptionException
    {
        printer.print("Enter ticket price (float & greater than 0): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput))
		{
            if (mode == ParserMode.CREATE)
			{
                printer.println(
                        "You're not able to insert a null value for float variable. Try another value");
                proposeContinue();
                return parseTicketPrice();
            }
			else
			{
                return null;
            }
        }

        try
        {
            float x = Float.parseFloat(userInput);
            TicketValidator.validateTicketPrice(x);
            return x;
        }
		catch (NumberFormatException e)
		{
            printer.println("Ticket coordinate should be a float.");
            proposeContinue();
            return parseTicketPrice();
        }
		catch (ValidationException e)
		{
            printer.println(e.getMessage());
            proposeContinue();
            return parseTicketPrice();
        }
    }

    /**
     * Method can parse ticket comment
     *
     * @return ticket comment
     * @throws UserInterruptionException if got not y/Y from user
     */
    private String parseTicketComment() throws UserInterruptionException
    {
        printer.print("Enter ticket comment (non-empty): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput))
		{
            if (mode == ParserMode.CREATE)
			{
                printer.println(
                        "You're not able to insert a null value for this variable. Try another value");
                proposeContinue();
                return parseTicketComment();
            }
			else
			{
                return null;
            }
        }

        try
        {
            TicketValidator.validateTicketComment(userInput);
            return userInput;
        }
		catch (Exception e)
		{
            printer.println(e.getMessage());
            proposeContinue();
            return parseTicketComment();
        }
    }

    /**
     * Method can parse ticket refundable status
     *
     * @return ticket refundable
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Boolean parseTicketRefundable() throws UserInterruptionException
    {
        printer.print("Is ticket refundable (y/Y - for yes, other - for no): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput) && mode == ParserMode.UPDATE) return null;

        return "Y".equalsIgnoreCase(userInput);
    }

    /**
     * Method can parse ticket type
     *
     * @return ticket type
     * @throws UserInterruptionException if got not y/Y from user
     */
    private TicketType parseTicketType() throws UserInterruptionException
    {
        printer.print(
                String.format("Enter ticket type (can be null) %s: ", Arrays.toString(TicketType.values()))
        );

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput)) return null;

        try
        {
            return TicketType.valueOf(userInput);
        }
		catch (IllegalArgumentException e)
		{
            printer.println("You should select ticket type from the listed ones.");
            proposeContinue();
            return parseTicketType();
        }
    }

    /**
     * Method can parse venue
     *
     * @return venue
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Venue parseTicketVenue() throws UserInterruptionException
    {
        venueCLIParser.setMode(mode);
        return venueCLIParser.parseVenue();
    }

    /**
     * Crate an instance of Ticket and return it
     *
     * @return ticket instance
     * @throws UserInterruptionException if input stream ended or process interrupted by user
     */
    public Ticket parseTicket() throws UserInterruptionException
    {
        printer.println("#### ENTERING TICKET ####");
        String name = parseTicketName();
        Coordinates coordinates = parseTicketCoordinates();
        LocalDateTime creationDate = LocalDateTime.now(ZoneId.systemDefault());
        Float price = parseTicketPrice();
        String comment = parseTicketComment();
        Boolean ref = parseTicketRefundable();
        TicketType type = parseTicketType();
        Venue venue = parseTicketVenue();
        printer.println("#### ENTERING TICKET ENDED ####");

        return new Ticket(name, coordinates, creationDate, price, comment, ref, type, venue);
    }
}