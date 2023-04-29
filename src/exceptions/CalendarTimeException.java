package exceptions;

import static models.CalendarEvent.TIME_FORMATTER;

public class CalendarTimeException extends CalendarException{
    public CalendarTimeException() {
        super("Invalid time format. Please use "+ TIME_FORMATTER);
    }
}
