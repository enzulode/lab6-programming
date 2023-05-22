package com.enzulode.common.validation;

import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Coordinates;

/** Utility class for coordinates validation */
public class CoordinatesValidator
{
	/**
	 * This method validates x coordinate
	 *
	 * @param x x coordinate
	 * @throws ValidationException if validation not succeed
	 */
	public static void validateXCoord(Float x) throws ValidationException
	{
		if (x == null) throw new ValidationException("X coordinate cannot be null");

		if (Float.compare(x, -390) <= 0) throw new ValidationException("X coordinate should be greater than -390");

		if (x == Float.POSITIVE_INFINITY || x == Float.NEGATIVE_INFINITY)
			throw new ValidationException("X coordinate cannot be an infinity");
	}

	/**
	 * This method validates y coordinate
	 *
	 * @param y y coordinate
	 * @throws ValidationException if y coordinate is null
	 */
	public static void validateYCoord(Integer y) throws ValidationException
	{
		if (y == null) throw new ValidationException("Y coordinate cannot be null");
	}

	/**
	 * Coordinates object validation method
	 *
	 * @param coordinates coordinates object under validation
	 * @throws ValidationException if x coordinate is less or equal to -390 or is an infinity or if coordinates
	 * instance is null
	 */
	public static void validateCoordinates(Coordinates coordinates) throws ValidationException
	{
		if (coordinates == null) throw new ValidationException("Coordinates cannot be null");

		validateXCoord(coordinates.getX());
		validateYCoord(coordinates.getY());
	}
}
