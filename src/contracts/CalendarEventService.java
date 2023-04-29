package contracts;

import models.CalendarEvent;

public interface CalendarEventService {
    boolean checkCompatibility(CalendarEvent calendarEvent);
}
