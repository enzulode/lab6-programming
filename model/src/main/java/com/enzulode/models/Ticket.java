package com.enzulode.models;

import com.enzulode.models.util.TicketType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/** This data class contains ticket information Is stored in the database */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Ticket implements Comparable<Ticket>, Serializable
{
	/**
	 * This field contains unique ticket id
	 *
	 */
	@Setter
	private Integer id;

	/**
	 * This fields contains ticket name
	 *
	 */
	private final String name;

	/**
	 * This field contains ticket coordinates
	 *
	 */
	private final Coordinates coordinates;

	/**
	 * This field contains ticket creation date
	 *
	 */
	private final LocalDateTime creationDate;

	/**
	 * This field contains ticket price
	 *
	 */
	private final Float price;

	/**
	 * This field contains ticket comment
	 *
	 */
	private final String comment;

	/**
	 * This field contains ticket refundable status
	 *
	 */
	private final Boolean refundable;

	/**
	 * This field contains ticket type (can be null)
	 *
	 */
	private final TicketType type;

	/**
	 * This field contains ticket venue
	 *
	 */
	private final Venue venue;

	/**
	 * This method returns a comparison result for this object and the specified one
	 *
	 * @param obj the object to be compared
	 * @return comparison result (default comparison strategy with: less, equals and greater than
	 *     zero)
	 */
	@Override
	public int compareTo(@NonNull Ticket obj) {
		if (this.equals(obj)) return 0;
		if (this.name == null) return -1;
		if (obj.name == null) return 1;
		return this.name.compareTo(obj.name);
	}

	/**
	 * This method provides access to a string representation of ticket class object
	 *
	 * @return ticket string representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ticket id: ").append(id).append('\n');
		sb.append("Ticket name: ").append(name).append('\n');
		sb.append("Ticket coordinates: ").append('\t').append(coordinates).append('\n');
		sb.append("Ticket creation date: ").append(creationDate).append('\n');
		sb.append("Ticket price: ").append(price).append('\n');
		sb.append("Ticket comment: ").append(comment).append('\n');
		sb.append("Ticket refundable status: ").append(refundable).append('\n');
		sb.append("Ticket type: ").append((type == null) ? "not currently set" : type).append('\n');
		sb.append("Ticket venue: ").append('\t').append(venue).append('\n');

		return sb.toString();
	}
}
