package models.operations.manipulations;

import contracts.CalendarOperation;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;
import exceptions.OperationException;
import models.calendar.Calendar;
import models.calendar.CalendarEvent;

import java.util.ArrayList;

/**
 * The Unbook class implements the CalendarOperation interface to represent an operation that unbooks a previously booked event from a Calendar.
 */
public class Unbook implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the Unbook class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Unbook(Calendar calendar, ArrayList<String> instructions) {
        this.calendar=calendar;
        this.instructions=instructions;
    }

    /**
     * Executes the unbook operation by creating a new CalendarEvent object from the instructions and removing it from the Calendar instance.
     * @throws OperationException if the specified date or time is invalid or if there is no event booked for the specified date and time.
     */
    @Override
    public void execute() throws OperationException {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        CalendarEvent calendarEvent;

        try {
            calendarEvent =new CalendarEvent(null,date,startTime,endTime,null);

        } catch (CalendarDateException | CalendarTimeException | InvalidTimeIntervalException e) {
            throw new OperationException(e.getMessage());
        }

        if(calendar.getCalendarEvents().removeIf(calendarEvent::equals))
            System.out.printf("Event successfully unbooked:  %s %s %s\n",date,startTime,endTime);
        else
            throw new OperationException("\nThere is no such event booked: "+date+" "+startTime+" "+endTime);
    }

}
