package personalCalendar.exceptions;

import static personalCalendar.models.CalendarEvent.TIME_FORMATTER;

public class CalendarTimeException extends Exception{
    public CalendarTimeException() {
        super("Invalid time format. Please use "+ TIME_FORMATTER);
    }
}
