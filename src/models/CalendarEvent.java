package models;

import adapters.LocalDateAdapter;
import adapters.LocalTimeAdapter;
import contracts.CalendarEventService;
import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class CalendarEvent  implements CalendarEventService, Comparable<CalendarEvent> {

    private String name;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime startTime;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime endTime;
    private String note;
    @XmlAttribute
    private boolean isHoliday;


    //region Constants
    public static final String DATE_PATTERN="dd-MM-yyyy";
    public static final String TIME_PATTERN="HH:mm";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    //endregion


    public CalendarEvent(){}

    public CalendarEvent(String eventName, String date, String startTime, String endTime, String note) throws InvalidTimeIntervalException, CalendarDateException, CalendarTimeException {

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

        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }

        if(this.date.getDayOfWeek()== DayOfWeek.SATURDAY||this.date.getDayOfWeek()==DayOfWeek.SUNDAY)
            this.isHoliday=true;

        this.name = eventName;
        this.note = note;
    }

    public CalendarEvent(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, String note) throws InvalidTimeIntervalException, CalendarDateException, CalendarTimeException {
        this.name=eventName;
        this.date = date;
        this.startTime=startTime;
        this.endTime = endTime;
        this.note=note;

        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }

        if(this.date.getDayOfWeek()== DayOfWeek.SATURDAY||this.date.getDayOfWeek()==DayOfWeek.SUNDAY)
            this.isHoliday=true;
    }

    public CalendarEvent(String date, String startTime) throws CalendarDateException, CalendarTimeException {
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

    public CalendarEvent(String date){
        this.date = LocalDate.parse(date, DATE_FORMATTER);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent event = (CalendarEvent) o;
        return date.equals(event.date) && (startTime.equals(event.startTime)||endTime.equals(event.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }

    @Override
    public int compareTo(CalendarEvent o) {
        return this.startTime.compareTo(o.startTime);
    }

    @Override
    public String toString() {
        if(isHoliday)
            return  String.format("%-25s",DATE_FORMATTER.format(date)+" (holiday)")+
                    String.format("%-15s", TIME_FORMATTER.format(startTime))+
                    String.format("%-15s", TIME_FORMATTER.format(endTime))+
                    String.format("%-30s", name)+
                    String.format("%-40s", note);

        return  String.format("%-25s",DATE_FORMATTER.format(date)+" (work day)")+
                String.format("%-15s", TIME_FORMATTER.format(startTime))+
                String.format("%-15s", TIME_FORMATTER.format(endTime))+
                String.format("%-30s", name)+
                String.format("%-40s", note);
    }

    //Проверяваме съвместимостта между две събития
    //Връща true при съвместимост
    public boolean checkCompatibility(CalendarEvent calendarEvent){

        if(calendarEvent.date.equals(this.date))
        {
            if(calendarEvent.startTime.isAfter(this.startTime)&&calendarEvent.startTime.isBefore(this.endTime))
                return false;

            if(calendarEvent.endTime.isAfter(this.startTime)&&calendarEvent.endTime.isBefore(this.endTime))
                return false;

            if(this.startTime.isAfter(calendarEvent.startTime)&&this.startTime.isBefore(calendarEvent.endTime))
                return false;

            return !this.endTime.isAfter(calendarEvent.startTime) || !this.endTime.isBefore(calendarEvent.endTime);
        }
        return true;
    }



    //region Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String eventName) {
        this.name = eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) throws InvalidTimeIntervalException {
        this.date = date;
        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) throws InvalidTimeIntervalException {
        this.startTime = startTime;
        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }
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

