package models.operations.inqueries;

import comparators.DurationComparator;
import contracts.CalendarOperation;
import exceptions.OperationException;
import models.calendar.Calendar;
import models.calendar.CalendarEvent;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.calendar.CalendarEvent.DATE_FORMATTER;

/**
 * A calendar operation that retrieves the busiest days within a given date range.
 */
public class Busydays implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the Busydays class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Busydays(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Retrieves the busiest days within a given date range and prints the result to standard output.
     * @throws OperationException if an error occurs during the execution of the operation
     */
    @Override
    public void execute() throws OperationException {
        LocalDate startDate;
        LocalDate endDate;

        Map<DayOfWeek,Duration> busydaysMap=new EnumMap<>(DayOfWeek.class);

        try {
            startDate = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
            endDate = LocalDate.parse(instructions.get(1), DATE_FORMATTER);
        }catch (DateTimeParseException e){
            throw new OperationException(e);
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
        DurationComparator comparator=new DurationComparator();

        busydays.sort(comparator.reversed());

        for(Map.Entry<DayOfWeek,Duration> entry:busydays){
            long hours=entry.getValue().toHours();
            long minutes=entry.getValue().toMinutes()%60;
            System.out.println(entry.getKey() + " - " + hours + "h " + minutes + "m");
        }
    }
}