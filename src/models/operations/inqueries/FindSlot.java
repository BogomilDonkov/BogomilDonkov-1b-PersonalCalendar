package models.operations.inqueries;

import contracts.DefaultOperation;
import exceptions.OperationException;
import models.calendar.Calendar;
import models.calendar.CalendarEvent;
import models.operations.util.TimeGap;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.calendar.CalendarEvent.DATE_FORMATTER;

/**
 * This class represents an operation to find free time slots in a given calendar based on provided instructions.
 * Implements the Operation interface, which requires the implementation of the execute method.
 */
public class FindSlot implements DefaultOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the FindSlot class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public FindSlot(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Executes the FindSlot operation by finding free time slots in the calendar and printing them to the console.
     * Throws an OperationException if there are no free time slots in the calendar.
     * @throws OperationException if there are no free time slots in the calendar.
     */
    @Override
    public void execute() throws OperationException {
        ArrayList<TimeGap> arrayList= findFreeSpaceInCalendar();

        if(arrayList.isEmpty())
            throw new OperationException("There is no free space in calendar");
        else {
            System.out.println("\nThere are free spaces in : ");
            for (TimeGap pair : arrayList) {
                System.out.println("From " + pair.getStartTime() + " to " + pair.getEndTime());
            }
        }
    }

    /**
     * Finds and returns a list of free time slots in the calendar based on the instructions provided.
     * @return an ArrayList of TimeGap objects representing the free time slots in the calendar.
     * @throws OperationException if there is an error parsing the instructions or the calendar events.
     */
    public ArrayList<TimeGap> findFreeSpaceInCalendar() throws OperationException {
        ArrayList<TimeGap> freeTimeGaps=new ArrayList<>();
        double hours;
        LocalDate date;
        try {
            hours = Double.parseDouble(instructions.get(1));
            date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (NumberFormatException | NullPointerException e){
            throw new OperationException("Hours argument must have numeric value!");
        }catch (DateTimeParseException e){
            throw new OperationException(e);
        }

        LocalTime startTime = LocalTime.parse("08:00");
        LocalTime endTime = LocalTime.parse("17:00");
        Duration duration;

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        //Намираме и запазваме всички събития от календара, зареден в програмата, с подадената дата от потребителя в колекция
        List<CalendarEvent> filteredCalendarEvents =
                new ArrayList<>(calendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());
        Collections.sort(filteredCalendarEvents);
        for(CalendarEvent event:filteredCalendarEvents) {
            duration = Duration.between(startTime,event.getStartTime());
            double difference = duration.toHours() + (double) (duration.toMinutes() % 60) / 60;
            if (difference >= hours) {
                freeTimeGaps.add(new TimeGap(startTime,event.getStartTime()));
            }
            startTime=event.getEndTime();
        }

        //правим проверка за последния времеви диапазон
        duration = Duration.between(startTime, endTime);
        double difference = duration.toHours() + (double) (duration.toMinutes() % 60) / 60;
        if (difference >= hours) {
            freeTimeGaps.add(new TimeGap(startTime,endTime));
        }
        return freeTimeGaps;
    }
}
