package project.exceptions;

/**
 * Exception thrown when an invalid date format is used.
 */
public class CalendarDateException extends CalendarException {

    /**
     * Constructs a new CalendarDateException with the specified detail message.
     * @param message the detail message
     */
    public CalendarDateException(String message) {
        super(message);
    }
}
