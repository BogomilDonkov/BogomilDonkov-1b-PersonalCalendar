package operations.calendarOp;

import contracts.CalendarOperation;
import exceptions.OperationException;
import models.Calendar;
import models.CalendarEvent;

import java.time.LocalDate;
import java.util.ArrayList;

import static models.CalendarEvent.DATE_FORMATTER;

/**
 * The Holiday class represents an operation to set a date to be a holiday.
 */
public class Holiday implements CalendarOperation {

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
    public Holiday(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Sets the specified date to be a holiday in the calendar.
     * @throws OperationException if the date is already a holiday in the calendar
     */
    @Override
    public void execute() throws OperationException {
        LocalDate date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        for(CalendarEvent event:calendar.getCalendarEvents()){
            if(event.getDate().equals(date)){
                if(event.isHoliday())
                    throw new OperationException("That date is already holiday");
                else
                    event.setHoliday(true);
            }
        }
        System.out.println("The date is set to holiday");
    }
}
