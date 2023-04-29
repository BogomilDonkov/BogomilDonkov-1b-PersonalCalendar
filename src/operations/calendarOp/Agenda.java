package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import models.Calendar;
import models.CalendarEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static models.CalendarEvent.DATE_FORMATTER;

public class Agenda implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Agenda(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute(){
        LocalDate date;

        try {
            date = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (DateTimeParseException ignored) {
            CalendarDateException e= new CalendarDateException();
            System.out.println(e.getMessage());
            return false;
        }

        List<CalendarEvent> eventsForSort=new ArrayList<>(calendar.getCalendarEvents());

        if(eventsForSort.isEmpty())
        {
            System.out.println("There are no events within the current set date: "+ DATE_FORMATTER.format(date));
            return false;
        }

        Collections.sort(eventsForSort);
        System.out.println("Date\t\t\tStartTime\tEndTime\t\tName\t\t\tNote");

        for(CalendarEvent event:eventsForSort){
            if(event.getDate().equals(date)){
                System.out.println(event);
            }
        }

        return true;
    }
}
