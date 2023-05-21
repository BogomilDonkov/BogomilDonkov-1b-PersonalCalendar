package project.exceptions;


import project.models.calendar.TimeInterval;

/**
 * Thrown to indicate that a given time format is invalid for calendar event.
 */
public class CalendarTimeException extends CalendarException{
    /**
     * Constructs a CalendarTimeException with a default message indicating the expected time format.
     * @param message exception message
     */
    public CalendarTimeException(String message) {
        super(message);
    }
}
