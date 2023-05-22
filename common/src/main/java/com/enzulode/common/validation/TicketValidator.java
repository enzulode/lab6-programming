package com.enzulode.common.validation;

import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Ticket;

import java.time.LocalDateTime;

/** Utility class for ticket validation */
public class TicketValidator
{
    /**
     * Validates ticket id
     *
     * @param id ticket id
     * @throws ValidationException if ticket id is null or if ticket id is less than or equals to zero
     */
    public static void validateTicketId(Integer id) throws ValidationException
    {
        if (id == null) throw new ValidationException("Ticket id cannot be null");

        if (id <= 0) throw new ValidationException("Ticket id should be greater than zero");
    }

    /**
     * Validates ticket name for being non-empty and non-null
     *
     * @param name ticket name
     * @throws ValidationException if ticket name is null or if ticket name is empty
     */
    public static void validateTicketName(String name) throws ValidationException
    {
        if (name == null) throw new ValidationException("Ticket name should be specified");

        if ("".equals(name)) throw new ValidationException("Ticket name cannot be empty");
    }

    /**
     * Validates a ticket creation date for being non-null
     *
     * @param creationDate ticket creation date
     * @throws ValidationException if ticket creation date is null
     */
    public static void validateTicketCreationDate(LocalDateTime creationDate) throws ValidationException
    {
        if (creationDate == null)
            throw new ValidationException("Ticket creation date should be specified");
    }

    /**
     * Validates a ticket price being greater than zero
     *
     * @param price ticket price
     * @throws ValidationException if ticket price is less than or equal to zero or if ticket price is null
     */
    public static void validateTicketPrice(Float price) throws ValidationException
    {
        if (price == null) throw new ValidationException("Ticket price cannot be null");

        if (Float.compare(price, 0) <= 0)
            throw new ValidationException("Ticket price should be greater than zero");
    }

    /**
     * Validates ticket comment exists and ticket is not longer than 404 symbols
     *
     * @param comment ticket comment
     * @throws ValidationException  if ticket comment is null or if ticket length is greater than 404
     */
    public static void validateTicketComment(String comment) throws ValidationException
    {
        if (comment == null)
            throw new ValidationException("Ticket comment should be specified");

        if (comment.length() > 404)
            throw new ValidationException("Ticket comment should be shorter than 405");
    }

    /**
     * Validates ticket refundable status being non-null
     *
     * @param refundable ticket refundable status
     * @throws ValidationException if ticket refundable status is null
     */
    public static void validateTicketRefundable(Boolean refundable) throws ValidationException
    {
        if (refundable == null)
            throw new ValidationException("Ticket refundable field cannot be null");
    }

    /**
     * Validates ticket instance
     *
     * @param ticket ticket instance
     * @throws ValidationException if some fields are out of bound or if some non-null fields are null or
     * if some non-empty fields are empty
     */
    public static void validateTicket(Ticket ticket) throws ValidationException
    {
        if (ticket == null) throw new ValidationException("Ticket cannot be null");

        validateTicketName(ticket.getName());
        CoordinatesValidator.validateCoordinates(ticket.getCoordinates());
        validateTicketCreationDate(ticket.getCreationDate());
        validateTicketPrice(ticket.getPrice());
        validateTicketComment(ticket.getComment());
        validateTicketRefundable(ticket.getRefundable());
        VenueValidator.validateVenue(ticket.getVenue());
    }
}
