package com.enzulode.client.parser.cli;

import com.enzulode.client.parser.exception.UserInterruptionException;
import com.enzulode.client.util.Printer;
import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.validation.VenueValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Venue;
import com.enzulode.models.util.VenueType;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Provides an ability to parse coordinates object from console
 *
 */
public final class VenueCLIParser extends CLIParser<Venue>
{
	/**
	 * Venue cli parser constructor
	 *
	 * @param printer printer instance
	 * @param scanner scanner instance
	 */
	public VenueCLIParser(@NonNull Printer printer, @NonNull Scanner scanner)
	{
		super(printer, scanner);
	}

	/**
	 * Method can parse venue name
	 *
	 * @return parsed venue name
	 * @throws UserInterruptionException if got not y/Y from user
	 */
	private String parseVenueName() throws UserInterruptionException
	{
		printer.print("Enter venue name (non-empty): ");

		if (!scanner.hasNextLine())
			throw new UserInterruptionException("Data input successfully interrupted");

		String userInput = scanner.nextLine().trim();

		if ("".equals(userInput))
		{
			if (mode == ParserMode.CREATE)
			{
				printer.println("You're not able to insert a null value for this variable. Try another value");
				proposeContinue();
				return parseVenueName();
			}
			else
			{
				return null;
			}
		}

		try
		{
			VenueValidator.validateVenueName(userInput);
			return userInput;
		}
		catch (ValidationException e)
		{
			printer.println(e.getMessage());
			proposeContinue();
			return parseVenueName();
		}
	}

	/**
	 * Method can parse venue capacity
	 *
	 * @return venue capacity
	 * @throws UserInterruptionException if got y/Y from user
	 */
	private Integer parseVenueCapacity() throws UserInterruptionException
	{
		printer.print("Enter venue capacity (Integer & greater than 0): ");

		if (!scanner.hasNextLine())
			throw new UserInterruptionException("Data input successfully interrupted");

		String userInput = scanner.nextLine().trim();

		if ("".equals(userInput))
		{
			if (mode == ParserMode.CREATE)
			{
				printer.println("This variable cannot be null. Try another value.");
				proposeContinue();
				return parseVenueCapacity();
			}
			else
			{
				return null;
			}
		}

		try
		{
			Integer capacity = Integer.parseInt(userInput);
			VenueValidator.validateVenueCapacity(capacity);
			return capacity;
		}
		catch (NumberFormatException e)
		{
			printer.println("Venue capacity should be an Integer.");
			proposeContinue();
			return parseVenueCapacity();
		}
		catch (ValidationException e)
		{
			printer.println(e.getMessage());
			proposeContinue();
			return parseVenueCapacity();
		}
	}

	/**
	 * Method can parse venue type
	 *
	 * @return venue type
	 * @throws UserInterruptionException if got y/Y from user
	 */
	private VenueType parseVenueType() throws UserInterruptionException
	{
		printer.print(
				String.format("Enter venue type (can be null) %s: ", Arrays.toString(VenueType.values()))
		);

		if (!scanner.hasNextLine())
			throw new UserInterruptionException("Data input successfully interrupted");

		String userInput = scanner.nextLine().trim();

		if ("".equals(userInput)) return null;

		try
		{
			return VenueType.valueOf(userInput);
		}
		catch (IllegalArgumentException e)
		{
			printer.println("You should select type from the listed ones.");
			proposeContinue();
			return parseVenueType();
		}
	}

	/**
	 * Creates an instance of Venue and return it
	 *
	 * @return instance of venue
	 * @throws UserInterruptionException if input stream ended or process interrupted by user
	 */
	public Venue parse() throws UserInterruptionException
	{
		printer.println("#### ENTERING VENUE ####");
		String name = parseVenueName();
		Integer capacity = parseVenueCapacity();
		VenueType type = parseVenueType();
		printer.println("#### ENTERING VENUE ENDED ####");

		return new Venue(name, capacity, type);
	}
}
