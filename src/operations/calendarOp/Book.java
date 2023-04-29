package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;
import models.Calendar;
import models.CalendarEvent;

import java.util.ArrayList;

public class Book implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Book(Calendar calendar, ArrayList<String> instructions) {
        this.calendar=calendar;
        this.instructions=instructions;
    }


    @Override
    public Boolean execute() {
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
                    System.out.printf("Event successfully booked:\n %s %s %s %s %s\n",name,date,startTime,endTime,note);
                else
                    System.out.printf("Event is already booked:\n %s %s %s %s %s\n",name,date,startTime,endTime,note);
            else
                System.out.printf("The event you have typed is currently incompatible with event:\n %s\n",incompatibleEvent);

            return true;
        } catch (CalendarDateException | InvalidTimeIntervalException | CalendarTimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
