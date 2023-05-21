package project.models.operations.inqueries;

import project.contracts.DefaultOperation;
import project.exceptions.CalendarDateException;
import project.exceptions.CalendarException;
import project.exceptions.CalendarTimeException;
import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.calendar.TimeInterval;
import project.models.parsers.LocalDateParser;
import project.models.parsers.LocalTimeParser;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This class represents an operation to find free time slots in a given calendar based on provided instructions.
 * Implements the Operation interface, which requires the implementation of the execute method.
 */
public class FindSlot implements DefaultOperation {

    /**
     * Name of the calendar
     */
    private String nameOfCalendar;
    /**
     * Date to search.
     */
    private LocalDate date;

    /**
     * Desired duration.
     */
    private double hours;

    /**
     * Start time limit.
     */
    private LocalTime startTime;

    /**
     * End time limit.
     */
    private LocalTime endTime;

    /**
     * Filtered calendar events.
     */
    private List<CalendarEvent> filteredCalendarEvents;


    /**
     * Constructs an instance of the FindSlot class with the specified Calendar and ArrayList of instructions.
     * @param personalCalendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     * @throws OperationException if hours input is invalid or if the inputed date is holiday
     * @throws CalendarDateException if the format is invalid
     * @throws CalendarTimeException if the format is invalid
     */
    public FindSlot(PersonalCalendar personalCalendar, List<String> instructions) throws OperationException, CalendarDateException, CalendarTimeException {

        nameOfCalendar=personalCalendar.getName();

        date= LocalDateParser.parse(instructions.get(0));

        for(CalendarEvent event:personalCalendar.getCalendarEvents()){
            if(event.getDate().equals(date)&&event.isHoliday())
                throw new OperationException("'FINDSLOT' search for free spaces only in work days!");
        }

        try {
            hours = Double.parseDouble(instructions.get(1));
        }catch (NumberFormatException | NullPointerException e){
            throw new OperationException("Hours argument must have numeric value!");
        }

        startTime= LocalTimeParser.parse("08:00");
        endTime=LocalTimeParser.parse("17:00");

        filteredCalendarEvents = new ArrayList<>(personalCalendar.getCalendarEvents().stream().filter(item -> item.getDate().equals(date)).toList());
    }

    /**
     * Executes the FindSlot operation by finding free time slots in the calendar and printing them to the console.
     * Throws an OperationException if there are no free time slots in the calendar.
     * @throws OperationException if there are no free time slots in the calendar.
     * @throws CalendarDateException if the input date is in invalid format.
     */
    @Override
    public void execute() throws OperationException, CalendarException {
        ArrayList<TimeInterval> timeIntervals= findFreeSpaceInCalendar();

        if(timeIntervals.isEmpty())
            throw new OperationException("There is no free space in calendar");

        printFoundedSlots(timeIntervals);
    }

    /**
     * Prints founded time intervals
     * @param arrayList - contains founded time intervals
     */
    private void printFoundedSlots(ArrayList<TimeInterval> arrayList){
        System.out.println("There are free spaces in "+ nameOfCalendar+": ");
        for (TimeInterval interval : arrayList) {
            System.out.println(interval);
        }
    }

    /**
     * Finds and returns a list of free time slots in the calendar based on the instructions provided.
     * @return an ArrayList of TimeGap objects representing the free time slots in the calendar.
     * @throws OperationException if there is an error parsing the instructions or the calendar events.
     * @throws CalendarDateException if the input date is in invalid format.
     */
    ArrayList<TimeInterval> findFreeSpaceInCalendar() throws CalendarException {
        ArrayList<TimeInterval> freeTimeIntervals =new ArrayList<>();

        Collections.sort(filteredCalendarEvents);

        Duration duration;

        for(CalendarEvent event:filteredCalendarEvents) {
            duration = Duration.between(startTime,event.getStartTime());
            double difference = duration.toHours() + (double) (duration.toMinutes() % 60) / 60;
            if (difference >= hours) {
                freeTimeIntervals.add(new TimeInterval(startTime,event.getStartTime()));
            }
            startTime=event.getEndTime();
        }

        duration = Duration.between(startTime, endTime);
        double difference = duration.toHours() + (double) (duration.toMinutes() % 60) / 60;
        if (difference >= hours) {
            freeTimeIntervals.add(new TimeInterval(startTime,endTime));
        }
        return freeTimeIntervals;
    }
}
