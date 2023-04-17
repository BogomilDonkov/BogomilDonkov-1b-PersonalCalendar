package personalCalendar.exceptions;

import static personalCalendar.models.CalendarEvent.DATE_PATTERN;

public class CalendarDateException extends Exception {
    public CalendarDateException() {
        super("Invalid date format. Please use "+ DATE_PATTERN);
    }
}
