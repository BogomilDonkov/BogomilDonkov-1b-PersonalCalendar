package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;
import models.Calendar;
import models.CalendarEvent;

import java.util.ArrayList;

public class Unbook implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Unbook(Calendar calendar, ArrayList<String> instructions) {
        this.calendar=calendar;
        this.instructions=instructions;
    }


    @Override
    public Boolean execute() {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        CalendarEvent calendarEvent;

        try {
            calendarEvent =new CalendarEvent(null,date,startTime,endTime,null);

        } catch (CalendarDateException | CalendarTimeException | InvalidTimeIntervalException e) {
            System.out.println(e.getMessage());
            return false;
        }

        if(calendar.getCalendarEvents().removeIf(calendarEvent::equals))
            System.out.printf("Event successfully unbooked:  %s %s %s\n",date,startTime,endTime);
        else
            System.out.printf("There is no such event booked: %s %s %s\n",date,startTime,endTime);

        return true;
    }

}
