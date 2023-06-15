package com.enzulode.models;

import com.enzulode.models.util.VenueType;
import lombok.*;

import java.io.Serializable;

/** This data class contains venue information Is used in Ticket data class */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Venue implements Serializable
{
	/**
	 * This field contains unique venue id
	 *
	 */
	private long id;

	/**
	 * This field contains venue name
	 *
	 */
	private final String name;

	/**
	 * This field contains venue capacity
	 *
	 */
	private final Integer capacity;

	/**
	 * This field contains venue type (can be null)
	 *
	 */
	private final VenueType type;

	/**
	 * This method provides access to a string representation of venue class object
	 *
	 * @return venue string representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VenueID: ").append(id).append('\n');
		sb.append("Venue name: ").append(name).append('\n');
		sb.append("Venue capacity: ").append(capacity).append('\n');
		sb.append("Venue type: ").append((type == null) ? "not currently set" : type).append('\n');

		return sb.toString();
	}
}
