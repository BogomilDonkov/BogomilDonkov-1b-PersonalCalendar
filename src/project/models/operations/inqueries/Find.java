package project.models.operations.inqueries;

import project.contracts.CalendarOperation;
import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents an operation for finding events in a calendar that contain a certain search string.
 * Implements the CalendarOperation interface.
 */
public class Find implements CalendarOperation {

    /**
     * Set of loaded calendar events.
     */
    private Set<CalendarEvent> calendarEvents;

    /**
     * String to search.
     */
    private StringBuilder stringToSearch;

    /**
     * Set of founded events.
     */
    private Set<CalendarEvent> foundedEvents;


    public Find(PersonalCalendar personalCalendar, ArrayList<String> instructions) {
        foundedEvents=new HashSet<>();
        calendarEvents=personalCalendar.getCalendarEvents();
        stringToSearch=new StringBuilder();

        for(String value:instructions) {
            stringToSearch.append(value);

            if(!value.equals(instructions.get(instructions.size()-1)))
                stringToSearch.append(" ");
        }
    }

    /**
     * Executes the operation to find events in the calendar that contain the specified search string.
     * Searches for events with names or notes that contain the search string case-insensitively.
     * If found, prints the events to the console.
     * @throws OperationException If no events are found that contain the search string.
     */
    @Override
    public void execute() throws OperationException {

        for(CalendarEvent event:calendarEvents){
            String eventNameCaseInsensitive=event.getName().toLowerCase();
            String eventNoteCaseInsensitive=event.getNote().toLowerCase();
            String stringToSearchCaseInsensitive=stringToSearch.toString().toLowerCase();

            if(eventNameCaseInsensitive.contains(stringToSearchCaseInsensitive)||eventNoteCaseInsensitive.contains(stringToSearchCaseInsensitive)){
                foundedEvents.add(event);
            }
        }

        if(foundedEvents.isEmpty())
            throw new OperationException("There are no events that contain: "+stringToSearch);


        System.out.println("Here are the events that contain '"+stringToSearch+"': ");
        printFoundedEvents(foundedEvents);
    }

    private void printFoundedEvents(Set<CalendarEvent> calendarEventSet){
        for(CalendarEvent event:calendarEventSet){
            System.out.println(event);
        }
    }

}
