package com.enzulode.common.validation;

import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Venue;

/** Utility class for venue validation */
public class VenueValidator
{
    /**
     * Method validates venue id
     *
     * @param id venue id under validation
     * @throws ValidationException if venue id is less than or equals to zero
     */
    public static void validateVenueId(long id) throws ValidationException
    {
        if (id <= 0) throw new ValidationException("Venue id should be greater than zero");
    }

    /**
     * Validates a venue name for being empty
     *
     * @param name venue name
     * @throws ValidationException if venue name is empty or if venue name is not specified
     */
    public static void validateVenueName(String name) throws ValidationException
    {
        if (name == null) throw new ValidationException("Venue name should be specified");

        if ("".equals(name)) throw new ValidationException("Venue name cannot be empty");
    }

    /**
     * Validates a venue capacity for being non-null and greater than zero
     *
     * @param capacity venue capacity
     * @throws ValidationException if venue capacity is null or if venue capacity is less than or equals to zero
     */
    public static void validateVenueCapacity(Integer capacity) throws ValidationException
    {
        if (capacity == null) throw new ValidationException("Venue capacity cannot be null");

        if (capacity <= 0)
            throw new ValidationException("Venue capacity should be greater than zero");
    }

    /**
     * Validates an existing venue instance
     *
     * @param venue venue instance
     * @throws ValidationException if some fields are out of bound or if some non-null fields are null or if some
     * non-empty fields are empty
     */
    public static void validateVenue(Venue venue) throws ValidationException
    {
        if (venue == null) throw new ValidationException("Venue cannot be null");

        validateVenueName(venue.getName());
        validateVenueCapacity(venue.getCapacity());
    }
}
