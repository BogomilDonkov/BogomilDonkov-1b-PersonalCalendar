package exceptions;

import static models.calendar.CalendarEvent.TIME_PATTERN;

/**
 * Thrown to indicate that a given time format is invalid for calendar event.
 */
public class CalendarTimeException extends CalendarException{
    /**
     * Constructs a CalendarTimeException with a default message indicating the expected time format.
     */
    public CalendarTimeException() {
        super("Invalid time format. Please use "+ TIME_PATTERN);
    }
}
