package exceptions;

/**
 * This exception is used to represent an error that occurred during the execution of a command operation in the calendar application.
 * It extends the CalendarException class, which is the base exception class for all calendar-related exceptions.
 */
public class OperationException extends CalendarException{

    /**
     * Constructs a new operation exception with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public OperationException(String message) {
        super(message);
    }

    /**
     * Constructs a new operation exception with the specified cause and a detail message of (cause==null ? null : cause.toString())
     * @param e the cause (which is saved for later retrieval by the {@link #getCause()} method). A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public OperationException(Exception e) {
        super(e);
    }
}
