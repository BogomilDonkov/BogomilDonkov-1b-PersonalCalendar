package exceptions;

/**
 * This exception class is a generic exception for the calendar application. It extends the Exception class.
 */
public class CalendarException extends Exception{

    /**
     * Constructs a new CalendarException with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public CalendarException(String message) {
        super(message);
    }
}
