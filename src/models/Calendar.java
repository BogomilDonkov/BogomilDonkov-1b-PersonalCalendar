package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class Calendar {
    private Set<CalendarEvent> calendarEvents;
    private final ArrayList<String> mergedCalendars;


    public Calendar() {
        calendarEvents=new HashSet<>();
        this.mergedCalendars=new ArrayList<>();
    }

    public Calendar(Set<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
        this.mergedCalendars=new ArrayList<>();
    }


    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();

        for(CalendarEvent event:calendarEvents){
            builder.append(event);
            builder.append("\n");
        }

        return builder.toString();
    }

    public boolean addEvent(CalendarEvent event){
        return calendarEvents.add(event);
    }

    public boolean remove(CalendarEvent event){
        return calendarEvents.remove(event);
    }

    public boolean addAll(Set<CalendarEvent> events){
        return calendarEvents.addAll(events);
    }

    public boolean isEmpty(){
        return calendarEvents.isEmpty();
    }

    public boolean addMergedCalendar(String calendarName) {
        return mergedCalendars.add(calendarName);
    }

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



    @XmlElement(name="event")
    public Set<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(Set<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public ArrayList<String> getMergedCalendars() {
        return mergedCalendars;
    }

}
