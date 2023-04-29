package exceptions;

import static models.CalendarEvent.DATE_PATTERN;

public class CalendarDateException extends CalendarException {
    public CalendarDateException() {
        super("Invalid date format. Please use "+ DATE_PATTERN);
    }
}
