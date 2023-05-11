package project.contracts;

import project.models.calendar.CalendarEvent;

/**
 * The CalendarEventService interface defines the methods that should be implemented by classes
 * that provide functionality to check the compatibility of a given CalendarEvent object.
 */
public interface CalendarEventService {

    /**
     * Checks the compatibility of the given CalendarEvent object with the current context.
     * @param calendarEvent the CalendarEvent object to be checked for compatibility.
     * @return true if the given CalendarEvent object is compatible with the current context, false otherwise.
     */
    boolean checkCompatibility(CalendarEvent calendarEvent);
}
