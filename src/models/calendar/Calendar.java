package models.calendar;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a Calendar containing a set of CalendarEvents and a list of merged calendars.
 * '@XmlRootElement' annotation is used to indicate that this class should be mapped to an XML element.
 */
@XmlRootElement
public class Calendar {

    /**
     * Set of all calendar events
     */
    private Set<CalendarEvent> calendarEvents;

    /**
     * List of all calendar names that were merged with this calendar
     */
    private final ArrayList<String> mergedCalendars;

    /**
     * Constructs a new Calendar object with an empty set of CalendarEvents and an empty list of merged calendars.
     */
    public Calendar() {
        this.calendarEvents=new HashSet<>();
        this.mergedCalendars=new ArrayList<>();
    }

    /**
     * Constructs a new Calendar object with a specified set of CalendarEvents and an empty list of merged calendars.
     * @param calendarEvents the set of CalendarEvents to be included in this Calendar
     */
    public Calendar(Set<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
        this.mergedCalendars=new ArrayList<>();
    }

    /**
     * Returns a string representation of this Calendar object.
     * @return a string representation of this Calendar object
     */
    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();

        for(CalendarEvent event:calendarEvents){
            builder.append(event);
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * Adds a new CalendarEvent to this Calendar.
     * @param event the CalendarEvent to be added
     * @return true if the CalendarEvent was added successfully, false otherwise
     */
    public boolean addEvent(CalendarEvent event){
        return calendarEvents.add(event);
    }

    /**
     * Removes a specified CalendarEvent from this Calendar.
     * @param event the CalendarEvent to be removed
     * @return true if the CalendarEvent was removed successfully, false otherwise
     */
    public boolean remove(CalendarEvent event){
        return calendarEvents.remove(event);
    }

    /**
     * Adds a set of CalendarEvents to this Calendar.
     * @param events the set of CalendarEvents to be added
     * @return true if all CalendarEvents were added successfully, false otherwise
     */
    public boolean addAll(Set<CalendarEvent> events){
        return calendarEvents.addAll(events);
    }

    /**
     * Checks if this Calendar is empty.
     * @return true if this Calendar is empty, false otherwise
     */
    public boolean isEmpty(){
        return calendarEvents.isEmpty();
    }

    /**
     * Adds the name of a merged calendar to the list of merged calendars.
     * @param calendarName the name of the merged calendar to be added
     * @return true if the calendar name was added successfully, false otherwise
     */
    public boolean addMergedCalendar(String calendarName) {
        return mergedCalendars.add(calendarName);
    }

    /**
     * Checks if a specified CalendarEvent is compatible with all CalendarEvents in this Calendar.
     * @param calendarEvent the CalendarEvent to be checked for compatibility
     * @return a Set of incompatible CalendarEvents if the specified CalendarEvent is incompatible with any CalendarEvent in this Calendar, or an empty Set otherwise
     */
    public Set<CalendarEvent> checkIfAllEventAreCompatibleWithCalendar(CalendarEvent calendarEvent) {
        Set<CalendarEvent> incompatibleEvents=new HashSet<>();


        for(CalendarEvent event :calendarEvents)
        {
            if(!event.checkCompatibility(calendarEvent))
            {
                incompatibleEvents.add(event);
            }
        }

        return incompatibleEvents;
    }

    /**
     * Returns the set of CalendarEvents associated with this Calendar.
     * @return the set of CalendarEvents associated with this Calendar.
     */
    @XmlElement(name="event")
    public Set<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    /**
     * Sets the set of CalendarEvents for this Calendar.
     * @param calendarEvents the set of CalendarEvents to set for this Calendar.
     */
    public void setCalendarEvents(Set<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    /**
     * Returns the list of merged calendars.
     * @return the list of merged calendars.
     */
    public ArrayList<String> getMergedCalendars() {
        return mergedCalendars;
    }

}
