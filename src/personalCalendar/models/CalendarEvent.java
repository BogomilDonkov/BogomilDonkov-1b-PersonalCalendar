package personalCalendar.models;

import personalCalendar.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CalendarEvent implements Comparable<CalendarEvent>{
    //region Members
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note;
    private boolean isHoliday;
    //endregion

    //region Constants
    public static final String DATE_PATTERN="dd-MM-yyyy";
    public static final String TIME_PATTERN="HH:mm";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    //endregion

    //region Constructors
    public CalendarEvent(String name, String date, String startTime, String endTime, String note) throws CalendarDateException, CalendarTimeException, InvalidTimeIntervalException {

        try {
            this.date = LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e) {
            throw new CalendarDateException();
        }

        try {
            this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
            this.endTime = LocalTime.parse(endTime, TIME_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarTimeException();
        }

        if(this.startTime.isAfter(this.endTime))
            throw new InvalidTimeIntervalException();

        this.name = name;
        this.note = note;
    }
    //endregion

    //region Constructors
    public CalendarEvent(String date, String startTime) throws CalendarDateException, CalendarTimeException{
        try{
        this.date = LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarDateException();
        }

        try {
            this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarTimeException();
        }

    }

    public CalendarEvent(String date) throws CalendarDateException{
        this.date = LocalDate.parse(date, DATE_FORMATTER);
    }
    //endregion



    //Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent event = (CalendarEvent) o;
        return date.equals(event.date) && (startTime.equals(event.startTime)||endTime.equals(event.endTime));
    }

    @Override
    public int compareTo(CalendarEvent o) {
        return this.startTime.compareTo(o.startTime);
    }

    @Override
    public String toString() {
        if(isHoliday)
            return DATE_FORMATTER.format(date) +"\t\t"+ TIME_FORMATTER.format(startTime) +"\t\t"+ TIME_FORMATTER.format(endTime) +"\t\t"+ name +"\t\t"+ note + "\t\t" +" holiday";

        return DATE_FORMATTER.format(date) +"\t\t"+ TIME_FORMATTER.format(startTime) +"\t\t"+ TIME_FORMATTER.format(endTime) +"\t\t"+ name +"\t\t"+ note + "\t\t" + "work day";
    }


    //Проверяваме съвместимостта между две събития
    public boolean checkCompatibility(CalendarEvent calendarEvent){

        if(calendarEvent.getDate().equals(this.date))
        {
            if(calendarEvent.startTime.isAfter(this.startTime)&&calendarEvent.startTime.isBefore(this.endTime))
            {
                return false;
            }

            if(calendarEvent.endTime.isAfter(this.startTime)&&calendarEvent.endTime.isBefore(this.endTime))
            {
                return false;
            }

            if(this.startTime.isAfter(calendarEvent.startTime)&&this.startTime.isBefore(calendarEvent.endTime))
            {
                return false;
            }

            if(this.endTime.isAfter(calendarEvent.startTime)&&this.endTime.isBefore(calendarEvent.endTime))
            {
                return false;
            }

            return true;
        }
        return true;
    }

    //region Accessors/Mutators
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }
    //endregion
}
