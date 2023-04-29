package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import models.Calendar;
import models.CalendarEvent;
import models.TimeGap;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.CalendarEvent.DATE_FORMATTER;

public class FindSlot implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;



    public FindSlot(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute() {

        ArrayList<TimeGap> arrayList= findFreeSpaceInCalendar();

        if(arrayList==null)
            return false;

        //Извеждаме подходящо съобщение
        if(arrayList.isEmpty())
            System.out.println("There is no free space in calendar");
        else {
            System.out.println("\nThere are free spaces in : ");
            for (TimeGap pair : arrayList) {
                System.out.println("From " + pair.getStartTime() + " to " + pair.getEndTime());
            }
        }

        return true;
    }

    public ArrayList<TimeGap> findFreeSpaceInCalendar(){
        ArrayList<TimeGap> freeTimeGaps=new ArrayList<>();
        double hours;
        LocalDate date;
        try {
            hours = Double.parseDouble(instructions.get(1));
            date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (NumberFormatException | NullPointerException e){
            System.out.println("Hours argument must have numeric value!");
            return null;
        }catch (DateTimeParseException e){
            System.out.println(new CalendarDateException().getMessage());
            return null;
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
