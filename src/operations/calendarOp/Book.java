package operations.calendarOp;

import contracts.CalendarOperation;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;
import exceptions.OperationException;
import models.Calendar;
import models.CalendarEvent;

import java.util.ArrayList;

import static models.CalendarEvent.DATE_FORMATTER;

/**
 * The Book class implements the CalendarOperation interface and represents an operation to book a new event into the calendar.
 */
public class Book implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs a Book object with the given calendar and instructions.
     * @param calendar the calendar instance to add the new event to.
     * @param instructions the list of instructions containing the information about the event to be booked.
     */
    public Book(Calendar calendar, ArrayList<String> instructions) {
        this.calendar=calendar;
        this.instructions=instructions;
    }

    /**
     * Executes the booking operation by creating a new CalendarEvent object from the given instructions and adding it to the calendar.
     * If the event is already booked or overlaps with another event,an OperationException is thrown.
     * @throws OperationException if the event is already booked or overlaps with another event or if
     * there is a problem with the date, start time, end time or note of the event.
     */
    @Override
    public void execute() throws OperationException {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        String name=instructions.get(3);

        StringBuilder noteBuilder=new StringBuilder();
        for(String string:instructions.subList(4,instructions.size()))
        {
            noteBuilder.append(string);
            noteBuilder.append(" ");
        }

        String note= noteBuilder.toString();

        try {
            CalendarEvent calendarEvent=new CalendarEvent(name,date,startTime,endTime,note);
            boolean isCompatible=true;
            CalendarEvent incompatibleEvent = null;

            for(CalendarEvent event:calendar.getCalendarEvents())
            {
                if(!calendarEvent.checkCompatibility(event))
                {
                    isCompatible=false;
                    incompatibleEvent=event;
                }
            }

            if(isCompatible)
                if(calendar.addEvent(calendarEvent))
                    System.out.println("Event successfully booked:\n "+calendarEvent.getDate().format(DATE_FORMATTER)+" "+calendarEvent.getStartTime()+" "
                            +calendarEvent.getEndTime()+" "+calendarEvent.getName()+" "+calendarEvent.getNote());
                else
                    System.out.println("Event is already booked:\n "+calendarEvent.getDate().format(DATE_FORMATTER)+" "+calendarEvent.getStartTime()+" "
                            +calendarEvent.getEndTime()+" "+calendarEvent.getName()+" "+calendarEvent.getNote());
            else
                throw new OperationException("The event you have typed is currently incompatible with event:\n"+incompatibleEvent);

        } catch (CalendarDateException | InvalidTimeIntervalException | CalendarTimeException e) {
            throw new OperationException(e.getMessage());
        }
    }
}
