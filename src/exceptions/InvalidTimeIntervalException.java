package exceptions;

public class InvalidTimeIntervalException extends CalendarException{

    public InvalidTimeIntervalException() {
        super("Incorrect input! Please note that endTime must be after startTime.");
    }
}
