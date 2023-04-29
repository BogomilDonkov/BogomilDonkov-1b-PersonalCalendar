package operations.calendarOp;

import contracts.CalendarOperation;
import exceptions.OperationException;
import models.Calendar;
import models.CalendarEvent;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A class that represents an operation for finding events in a calendar that contain a certain search string.
 * Implements the CalendarOperation interface.
 */
public class Find implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the Find class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Find(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Executes the operation to find events in the calendar that contain the specified search string.
     * Searches for events with names or notes that contain the search string case-insensitively.
     * If found, prints the events to the console.
     * @throws OperationException If no events are found that contain the search string.
     */
    @Override
    public void execute() throws OperationException {
        HashSet<CalendarEvent> foundedEvents=new HashSet<>();
        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        StringBuilder stringToSearch=new StringBuilder();
        int counter=0;
        for(String value:instructions) {
            stringToSearch.append(value);
            if(counter++!=instructions.size()-1)
                stringToSearch.append(" ");
        }

        for(CalendarEvent event:calendarEvents){
            String eventNameCaseInsensitive=event.getName().toLowerCase();
            String eventNoteCaseInsensitive=event.getNote().toLowerCase();
            String stringToSearchCaseInsensitive=stringToSearch.toString().toLowerCase();

            if(eventNameCaseInsensitive.contains(stringToSearchCaseInsensitive)||eventNoteCaseInsensitive.contains(stringToSearchCaseInsensitive)){
                foundedEvents.add(event);
            }
        }

        if(!foundedEvents.isEmpty()){
            System.out.println("Here are the events that contain '"+stringToSearch+"': ");
            for(CalendarEvent event:foundedEvents){
                System.out.println(event);
            }
        }
        else
            throw new OperationException("There are no events that contain: "+stringToSearch);
    }

}
