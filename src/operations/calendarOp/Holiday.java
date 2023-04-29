package operations.calendarOp;

import contracts.Operation;
import models.Calendar;
import models.CalendarEvent;

import java.time.LocalDate;
import java.util.ArrayList;

import static models.CalendarEvent.DATE_FORMATTER;

public class Holiday implements Operation<Void> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Holiday(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }


    @Override
    public Void execute() {
        LocalDate date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        for(CalendarEvent event:calendar.getCalendarEvents()){
            if(event.getDate().equals(date)){
                if(event.isHoliday()) {
                    System.out.println("That date is already holiday");
                    break;
                }
                else
                    event.setHoliday(true);
            }
        }
        System.out.println("The date is set to holiday");
        return null;
    }
}
