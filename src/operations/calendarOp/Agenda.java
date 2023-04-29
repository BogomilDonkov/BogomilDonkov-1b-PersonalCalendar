package operations.calendarOp;

import contracts.CalendarOperation;
import exceptions.OperationException;
import models.Calendar;
import models.CalendarEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static models.CalendarEvent.DATE_FORMATTER;
import static models.CalendarEvent.DATE_PATTERN;

/**
 * The Agenda class represents an operation that prints a list of all events for a specific date in the calendar, sorted by their start time.
 */
public class Agenda implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the Agenda class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Agenda(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Executes the Agenda operation, printing a list of all events for the specified date in the calendar, sorted by their start time.
     * @throws OperationException if there are no events for the specified date or if the date is in an invalid format.
     */
    @Override
    public void execute() throws OperationException {
        LocalDate date;

        try {
            date = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (DateTimeParseException ignored) {
            throw new OperationException("Invalid date format. Please use "+ DATE_PATTERN);
        }

        List<CalendarEvent> eventsForSort=new ArrayList<>();
        for(CalendarEvent event:calendar.getCalendarEvents()){
            if(event.getDate().equals(date)){
                eventsForSort.add(event);
            }
        }

        if(eventsForSort.isEmpty())
            throw new OperationException("There are no events within the current set date: "+ DATE_FORMATTER.format(date));

        Collections.sort(eventsForSort);

        System.out.println(String.format("%-25s","Date")+
                String.format("%-15s", "Start Time")+
                String.format("%-15s", "End Time")+
                String.format("%-30s", "Name")+
                String.format("%-40s", "Note"));

        for(CalendarEvent event:eventsForSort)
                System.out.println(event);
    }
}
