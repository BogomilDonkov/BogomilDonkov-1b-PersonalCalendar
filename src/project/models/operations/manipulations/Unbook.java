package project.models.operations.manipulations;

import project.contracts.CalendarOperation;
import project.exceptions.*;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.parsers.LocalDateParser;
import project.models.parsers.LocalTimeParser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

/**
 * The Unbook class implements the CalendarOperation interface to represent an operation that unbooks a previously booked event from a Calendar.
 */
public class Unbook implements CalendarOperation {

    /**
     * Loaded calendar events.
     */
    private Set<CalendarEvent> loadedCalendarEvents;
    /**
     * Date of the event to unbook
     */
    private LocalDate date;
    /**
     * Start time of the event to unbook.
     */
    private LocalTime startTime;
    /**
     * End time of the event to unbook.
     */
    private LocalTime endTime;


    /**
     * Constructs an instance of the Unbook class with the specified Calendar and ArrayList of instructions.
     * @param personalCalendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Unbook(PersonalCalendar personalCalendar, ArrayList<String> instructions) throws CalendarTimeException, CalendarDateException {

        this.loadedCalendarEvents=personalCalendar.getCalendarEvents();
        this.date= LocalDateParser.parse(instructions.get(0));
        this.startTime= LocalTimeParser.parse(instructions.get(1));
        this.endTime= LocalTimeParser.parse(instructions.get(2));
    }

    /**
     * Executes the unbook operation by creating a new CalendarEvent object from the instructions and removing it from the Calendar instance.
     * @throws OperationException if the specified date or time is invalid or if there is no event booked for the specified date and time.
     */
    @Override
    public void execute() throws OperationException, CalendarDateException, InvalidTimeIntervalException {
        CalendarEvent calendarEvent =new CalendarEvent("",date,startTime,endTime,"");


        if(loadedCalendarEvents.removeIf(calendarEvent::equals))
            System.out.printf("Event successfully unbooked:  %s %s %s\n",date,startTime,endTime);
        else
            throw new OperationException("\nThere is no such event booked: "+date+" "+startTime+" "+endTime);
    }

}
