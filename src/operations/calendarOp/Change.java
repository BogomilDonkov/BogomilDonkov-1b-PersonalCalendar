package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;
import models.Calendar;
import models.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import static models.CalendarEvent.DATE_FORMATTER;
import static models.CalendarEvent.TIME_FORMATTER;

public class Change implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Change(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute() {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String option=instructions.get(2);
        String newValue=instructions.get(3);

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        try {
            CalendarEvent newEvent=new CalendarEvent(date,startTime);

            for(CalendarEvent oldEvent:calendarEvents)
            {
                if(oldEvent.equals(newEvent)) {
                    newEvent=new CalendarEvent(oldEvent.getName(),oldEvent.getDate(),oldEvent.getStartTime(),oldEvent.getEndTime(),oldEvent.getNote());

                    checkOptionAndGetDecision(option,newValue,newEvent,oldEvent);
                    return true;
                }
            }
            System.out.println("There is no such event in the calendar.");
            return true;

        } catch (CalendarDateException | CalendarTimeException | InvalidTimeIntervalException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    //region Internal Methods

    private void checkAndUpdateCalendarEventSet(CalendarEvent newEvent,CalendarEvent oldEvent){
        boolean isCompatible=true;
        HashSet<CalendarEvent> incompatibleEvents = new HashSet<>();
        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        for(CalendarEvent event:calendarEvents)
        {
            if(event.equals(oldEvent))
                continue;

            if(!newEvent.checkCompatibility(event))
            {
                isCompatible=false;
                incompatibleEvents.add(event);
            }
        }

        if(isCompatible) {
            calendar.remove(oldEvent);
            calendar.addEvent(newEvent);
        }
        else {
            System.out.println("The event you have typed is currently incompatible with event\\s:");
            for(CalendarEvent event:incompatibleEvents){
                System.out.println(event);
            }
        }
    }

    private void checkOptionAndGetDecision(String option,String newValue,CalendarEvent newEvent,CalendarEvent oldEvent) throws InvalidTimeIntervalException {
        switch (option)
        {
            case "date"->{
                newEvent.setDate(LocalDate.parse(newValue, DATE_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            case "startTime" ->{
                newEvent.setStartTime(LocalTime.parse(newValue, TIME_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            case "endTime" ->{
                newEvent.setEndTime(LocalTime.parse(newValue, TIME_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            //Ако потребителя е избрал да промени името или бележката, няма нужда от валидация, затова директно обновяваме
            case "name" ->{
                oldEvent.setName(newValue);
            }

            case "note" ->{
                StringBuilder newValueBuilder = new StringBuilder(newValue);
                for(int i = 4; i<instructions.size(); i++)
                {
                    newValueBuilder.append(" ").append(instructions.get(i));
                }
                oldEvent.setNote(newValueBuilder.toString());
            }

            default -> System.out.println(option+" is not recognized as internal command.");
        }
    }

    //endregion
}
