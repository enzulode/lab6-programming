package com.enzulode.common.parser;

import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.models.util.TicketType;
import com.enzulode.models.util.VenueType;

/** Different types parser */
public class TypesParser
{

	/**
	 * Parse string from string
	 *
	 * @param line parsing string
	 * @return parsed string
	 */
	public static String parseString(String line)
	{
		if ("".equals(line.trim())) return null;

		return line.trim();
	}

	/**
	 * Parse integer from string
	 *
	 * @param line parsing string
	 * @return integer value
	 * @throws ParsingException if parsing not succeed
	 */
	public static Integer parseInteger(String line) throws ParsingException
	{
		if ("".equals(line.trim())) return null;

		try
		{
			return Integer.parseInt(line);
		}
		catch (NumberFormatException e)
		{
			throw new ParsingException("Inaproppriate argument type: should be integer", e);
		}
	}

	/**
	 * Parse long from string
	 *
	 * @param line parsing string
	 * @return long value
	 * @throws ParsingException if parsing not succeed
	 */
	public static Long parseLong(String line) throws ParsingException
	{
		try
		{
			return Long.parseLong(line);
		}
		catch (NumberFormatException e)
		{
			throw new ParsingException("Inaproppriate argument type: should be long");
		}
	}

	/**
	 * Parse boolean from string
	 *
	 * @param line parsing string
	 * @return boolean value
	 * @throws ParsingException if parsing not succeed
	 */
	public static Boolean parseBoolean(String line) throws ParsingException
	{
		if ("".equals(line.trim())) return null;

		if ("true".equalsIgnoreCase(line) || "y".equalsIgnoreCase(line))
		{
			return true;
		}

		return false;
	}

	/**
	 * Parse TicketType from string
	 *
	 * @param line parsing string
	 * @return TicketType value
	 * @throws ParsingException if parsing not succeed
	 */
	public static TicketType parseTicketType(String line) throws ParsingException
	{
		if ("".equals(line.trim())) return null;

		for (TicketType type : TicketType.values())
		{
			if (type.toString().equalsIgnoreCase(line.trim())) return TicketType.valueOf(line);
		}

		throw new ParsingException(
				"Inaproppriate argument type: should be TicketType or specified variable not found"
		);
	}

	/**
	 * Parse VenueType from string
	 *
	 * @param line parsing string
	 * @return VenueType value
	 * @throws ParsingException if parsing not succeed
	 */
	public static VenueType parseVenueType(String line) throws ParsingException
	{
		if ("".equals(line.trim())) return null;

		for (VenueType type : VenueType.values())
		{
			if (type.toString().equalsIgnoreCase(line)) return VenueType.valueOf(line);
		}

		throw new ParsingException(
				"Inaproppriate argument type: should be VenueType or specified variable not found."
		);
	}

	/**
	 * Parse float from string
	 *
	 * @param line parsing string
	 * @return float value
	 * @throws ParsingException if parsing not succeed
	 */
	public static Float parseFloat(String line) throws ParsingException
	{
		if ("".equals(line.trim())) return null;

		try
		{
			return Float.parseFloat(line);
		}
		catch (NumberFormatException e)
		{
			throw new ParsingException("Inaproppriate argument type: should be float.");
		}
	}

	/**
	 * Parse double from string
	 *
	 * @param line parsing string
	 * @return double value
	 * @throws ParsingException if parsing not succeed
	 */
	public static Double parseDouble(String line) throws ParsingException
	{
		try
		{
			return Double.parseDouble(line);
		}
		catch (NumberFormatException e)
		{
			throw new ParsingException("Inaproppriate argument type: should be float.");
		}
	}
}
