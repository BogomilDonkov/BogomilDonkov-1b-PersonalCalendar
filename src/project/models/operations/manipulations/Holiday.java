package project.models.operations.manipulations;

import project.contracts.CalendarOperation;
import project.exceptions.CalendarDateException;
import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.parsers.LocalDateParser;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Holiday class represents an operation to set a date to be a holiday.
 */
public class Holiday implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final PersonalCalendar personalCalendar;
    /**
     * Date to search.
     */
    private LocalDate date;


    /**
     * Constructs an instance of the Unbook class with the specified Calendar and ArrayList of instructions.
     * @param personalCalendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Holiday(PersonalCalendar personalCalendar, ArrayList<String> instructions) throws CalendarDateException {
        this.personalCalendar = personalCalendar;
        date= LocalDateParser.parse(instructions.get(0));
    }

    /**
     * Sets the specified date to be a holiday in the calendar.
     * @throws OperationException if the date is already a holiday in the calendar
     */
    @Override
    public void execute() throws OperationException {
        for(CalendarEvent event: personalCalendar.getCalendarEvents()){
            if(event.getDate().equals(date)){
                if(event.isHoliday())
                    throw new OperationException("That date is already holiday");
                else
                    event.setHoliday(true);
            }
        }
        System.out.println("The date is set to holiday");
        personalCalendar.addHoliday(date);
    }
}
