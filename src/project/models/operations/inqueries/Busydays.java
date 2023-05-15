package project.models.operations.inqueries;

import project.models.parsers.LocalDateParser;
import project.util.comparators.DurationComparator;
import project.contracts.CalendarOperation;
import project.exceptions.CalendarDateException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * A calendar operation that retrieves the busiest days within a given date range.
 */
public class Busydays implements CalendarOperation {

    /**
     * Begin of search date interval.
     */
    private LocalDate startDate;

    /**
     * End of search date interval.
     */
    private LocalDate endDate;

    /**
     * Set of loaded calendar events.
     */
    private Set<CalendarEvent> calendarEvents;

    /**
     * Map that contains values of {@link Duration} class that shows busyness of the current {@link DayOfWeek}
     */
    private Map<DayOfWeek,Duration> busydaysMap;


    public Busydays(PersonalCalendar personalCalendar, List<String> instructions) throws CalendarDateException {

        startDate= LocalDateParser.parse(instructions.get(0));
        endDate= LocalDateParser.parse(instructions.get(1));
        busydaysMap=new EnumMap<>(DayOfWeek.class);
        calendarEvents=personalCalendar.getCalendarEvents();
    }

    /**
     * Retrieves the busiest days within a given date range and prints the result to standard output.
     * @throws CalendarDateException if an error occurs during the execution of the operation
     */
    @Override
    public void execute() throws CalendarDateException {

        if(startDate.isAfter(endDate)){
            throw new CalendarDateException("Invalid date interval. Start date must be before end date.");
        }

        for(CalendarEvent event:calendarEvents){
            if((event.getDate().isAfter(startDate) || event.getDate().equals(startDate))) {
                if (event.getDate().isBefore(endDate)||event.getDate().equals(endDate)) {
                    Duration duration= Duration.between(event.getStartTime(),event.getEndTime());
                    busydaysMap.merge(event.getDate().getDayOfWeek(),duration,Duration::plus);
                }
            }
        }

        printBusydays();
    }

    private void printBusydays(){

        DurationComparator comparator=new DurationComparator();

        ArrayList<Map.Entry<DayOfWeek,Duration>> busydays=new ArrayList<>(busydaysMap.entrySet());

        busydays.sort(comparator.reversed());

        for(Map.Entry<DayOfWeek,Duration> entry:busydays){
            long hours=entry.getValue().toHours();
            long minutes=entry.getValue().toMinutes()%60;
            System.out.println(entry.getKey() + " - " + hours + "h " + minutes + "m");
        }
    }

}
