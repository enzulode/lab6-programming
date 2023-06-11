package com.enzulode.client.parser.cli;

import com.enzulode.client.parser.exception.UserInterruptionException;
import com.enzulode.client.util.Printer;
import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.validation.CoordinatesValidator;
import com.enzulode.models.Coordinates;
import lombok.NonNull;

import java.util.Scanner;

/**
 * Provides an ability to parse coordinates object from console
 *
 */
public final class CoordinatesCLIParser extends CLIParser<Coordinates>
{
    /**
     * Coordinates cli parser constructor
     *
     * @param printer printer instance
     * @param scanner scanner instance
     */
    public CoordinatesCLIParser(@NonNull Printer printer, @NonNull Scanner scanner)
    {
        super(printer, scanner);
    }

    /**
     * Method is parsing X coordinate
     *
     * @return float variable of X coordinate
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Float parseXCoord() throws UserInterruptionException
    {
        printer.print("Enter X coordinate (float & greater than -390): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput))
        {
            if (mode == ParserMode.CREATE)
            {
                printer.println("You're not able to insert a null value for float variable. Try another value.");
                proposeContinue();
                return parseXCoord();
            }
            else
            {
                return null;
            }
        }

        try
        {
            float x = Float.parseFloat(userInput);
            CoordinatesValidator.validateXCoord(x);
            return x;
        }
        catch (NumberFormatException e)
        {
            printer.println("X coordinate should be a float.");
            proposeContinue();
            return parseXCoord();
        }
        catch (Exception e)
        {
            printer.println(e.getMessage());
            proposeContinue();
            return parseXCoord();
        }
    }

    /**
     * Method is parsing Y coordinate
     *
     * @return float variable of Y coordinate
     * @throws UserInterruptionException if got not y/Y from user
     */
    private Integer parseYCoord() throws UserInterruptionException
    {
        printer.print("Enter Y coordinate (Integer): ");

        if (!scanner.hasNextLine())
            throw new UserInterruptionException("Data input successfully interrupted");

        String userInput = scanner.nextLine().trim();

        if ("".equals(userInput))
        {
            if (mode == ParserMode.CREATE)
            {
                printer.println("This variable cannot be null. Try another value.");
                proposeContinue();
                return parseYCoord();
            }
            else
            {
                return null;
            }
        }

        try
        {
            Integer y = Integer.parseInt(userInput);
            CoordinatesValidator.validateYCoord(y);
            return y;
        }
        catch (NumberFormatException e)
        {
            printer.println("X coordinate should be an Integer.");
            proposeContinue();
            return parseYCoord();
        }
        catch (Exception e)
        {
            printer.println(e.getMessage());
            proposeContinue();
            return parseYCoord();
        }
    }

    /**
     * Creates an instance of coordinates and returns it
     *
     * @return instance of coordinates
     * @throws UserInterruptionException if input stream ended or process interrupted by user
     */
    public Coordinates parse() throws UserInterruptionException
    {
        printer.println("#### ENTERING COORDINATES ####");
        Float x = parseXCoord();
        Integer y = parseYCoord();
        printer.println("#### ENTERING COORDINATES ENDED ####");

        return new Coordinates(x, y);
    }
}