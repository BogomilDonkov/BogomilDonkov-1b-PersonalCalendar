package exceptions;

/**
 * Thrown to indicate that a time interval is invalid because the end time is not after the start time.
 */
public class InvalidTimeIntervalException extends CalendarException{

    /**
     * Constructs an InvalidTimeIntervalException with a default message indicating that the end time must be after the start time.
     */
    public InvalidTimeIntervalException() {
        super("Incorrect input! Please note that endTime must be after startTime.");
    }
}
