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

/**
 * The Book class implements the CalendarOperation interface and represents an operation to book a new event into the calendar.
 */
public class Book implements CalendarOperation {

    /**
     * Current loaded {@link PersonalCalendar} object on which the operation will be executed.
     */
    private final PersonalCalendar personalCalendar;

    /**
     * Date of the event.
     */
    private LocalDate date;

    /**
     * Start time of the event.
     */
    private LocalTime startTime;
    /**
     * End time of the event.
     */
    private LocalTime endTime;

    /**
     * Name of the event.
     */
    private String name;
    /**
     * Note of the event.
     */
    private String note;


    /**
     * Constructs a Book object with the given calendar and instructions.
     * @param personalCalendar the calendar instance to add the new event to.
     * @param instructions the list of instructions containing the information about the event to be booked.
     */
    public Book(PersonalCalendar personalCalendar, ArrayList<String> instructions) throws CalendarDateException, CalendarTimeException {
        this.personalCalendar = personalCalendar;
        this.date= LocalDateParser.parse(instructions.get(0));
        this.startTime= LocalTimeParser.parse(instructions.get(1));
        this.endTime= LocalTimeParser.parse(instructions.get(2));
        this.name=instructions.get(3);
        this.note=instructions.get(4);
    }

    /**
     * Executes the booking operation by creating a new CalendarEvent object from the given instructions and adding it to the calendar.
     * If the event is already booked or overlaps with another event,an OperationException is thrown.
     * @throws OperationException if the event is already booked or overlaps with another event or if
     * there is a problem with the date, start time, end time or note of the event.
     */
    @Override
    public void execute() throws OperationException, CalendarDateException, InvalidTimeIntervalException {


        CalendarEvent calendarEvent=new CalendarEvent(name,date,startTime,endTime,note);
        boolean isCompatible=true;
        CalendarEvent incompatibleEvent = null;
        for(CalendarEvent event: personalCalendar.getCalendarEvents())
        {
            if(!calendarEvent.checkCompatibility(event))
            {
                isCompatible=false;
                incompatibleEvent=event;
            }
        }
        if(personalCalendar.getHolidays().contains(date))
            calendarEvent.setHoliday(true);
        if(isCompatible)
            if(personalCalendar.addEvent(calendarEvent))
                System.out.println("Event successfully booked:\n "+calendarEvent);
        else
            System.out.println("Event is already booked:\n "+calendarEvent);
        else
            throw new OperationException("The event you have typed is currently incompatible with event:\n"+incompatibleEvent);

    }
}
