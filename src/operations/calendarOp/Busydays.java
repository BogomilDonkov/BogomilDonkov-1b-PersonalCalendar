package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import models.Calendar;
import models.CalendarEvent;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.CalendarEvent.DATE_FORMATTER;

public class Busydays implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Busydays(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute() {
        LocalDate startDate;
        LocalDate endDate;

        Map<DayOfWeek,Duration> busydaysMap=new EnumMap<>(DayOfWeek.class);

        try {
            startDate = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
            endDate = LocalDate.parse(instructions.get(1), DATE_FORMATTER);
        }catch (DateTimeParseException e){
            CalendarDateException calendarDateException=new CalendarDateException();
            System.out.println(calendarDateException.getMessage());
            return false;
        }

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());
        for(CalendarEvent event:calendarEvents){
            if((event.getDate().isAfter(startDate) || event.getDate().equals(startDate))) {
                if (event.getDate().isBefore(endDate)||event.getDate().equals(endDate)) {
                    Duration duration= Duration.between(event.getStartTime(),event.getEndTime());
                    busydaysMap.merge(event.getDate().getDayOfWeek(),duration,Duration::plus);
                }
            }
        }

        ArrayList<Map.Entry<DayOfWeek,Duration>> busydays=new ArrayList<>(busydaysMap.entrySet());
        Comparator<Map.Entry<DayOfWeek, Duration>> durationComparator =
                Comparator.comparing(Map.Entry<DayOfWeek, Duration>::getValue).reversed();
        busydays.sort(durationComparator);

        for(Map.Entry<DayOfWeek,Duration> entry:busydays){
            long hours=entry.getValue().toHours();
            long minutes=entry.getValue().toMinutes()%60;
            System.out.println(entry.getKey() + " - " + hours + "h " + minutes + "m");
        }

        return true;
    }
}
