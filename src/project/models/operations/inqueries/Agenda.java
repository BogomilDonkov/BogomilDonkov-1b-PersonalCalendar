package project.models.operations.inqueries;

import project.contracts.CalendarOperation;
import project.exceptions.CalendarDateException;
import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.parsers.LocalDateParser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The Agenda class represents an operation that prints a list of all events for a specific date in the calendar, sorted by their start time.
 */
public class Agenda implements CalendarOperation {

    /**
     * Date to search
     */
    private LocalDate date;

    /**
     * Set of loaded calendar events.
     */
    private Set<CalendarEvent> calendarEvents;

    /**
     * Constructs a Agenda object with the given calendar and instructions.
     * @param personalCalendar the calendar instance to add the new event to.
     * @param instructions the list of instructions containing the information about the event to be booked.
     * @throws CalendarDateException if the format is invalid
     */
    public Agenda(PersonalCalendar personalCalendar, List<String> instructions) throws CalendarDateException {
        date= LocalDateParser.parse(instructions.get(0));
        calendarEvents=personalCalendar.getCalendarEvents();
    }

    /**
     * Executes the Agenda operation, printing a list of all events for the specified date in the calendar, sorted by their start time.
     * @throws OperationException if there are no events for the specified date or if the date is in an invalid format.
     */
    @Override
    public void execute() throws OperationException {

        List<CalendarEvent> eventsForSort=new ArrayList<>();
        for(CalendarEvent event: calendarEvents){
            if(event.getDate().equals(date)){
                eventsForSort.add(event);
            }
        }

        if(eventsForSort.isEmpty())
            throw new OperationException("There are no events within the current set date: "+ LocalDateParser.format(date));

        Collections.sort(eventsForSort);

        printFoundedEvents(eventsForSort);
    }

    /**
     * Prints founded events
     * @param calendarEvents - sorted list of founded events
     */
    private void printFoundedEvents(List<CalendarEvent> calendarEvents){
        System.out.println(String.format("%-25s","Date")+
                String.format("%-15s", "Start Time")+
                String.format("%-15s", "End Time")+
                String.format("%-30s", "Name")+
                String.format("%-40s", "Note"));

        for(CalendarEvent event:calendarEvents)
            System.out.println(event);
    }
}
