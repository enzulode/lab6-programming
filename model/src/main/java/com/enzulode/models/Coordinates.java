package com.enzulode.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This data class contains coordinates information Is used in Ticket data class
 *
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Coordinates implements Serializable
{
	/**
	 * This field contains X coordinate
	 *
	 */
	private final Float x;

	/**
	 * This field contains Y coordinate
	 *
	 */
	private final Integer y;

	/**
	 * This method provides access to a string representation of coordinates class object
	 *
	 * @return coordinates string representation
	 */
	@Override
	public String toString()
	{
		return String.format("Coordinates: [x: %f, y: %d]", x, y);
	}
}