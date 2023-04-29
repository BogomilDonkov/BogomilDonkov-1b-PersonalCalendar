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

/**
 * Represents a calendar event.
 * This class implements the CalendarEventService interface and also provides comparison
 * functionality through the Comparable interface. It can be used to store and manipulate
 * calendar events, and provides various constructors and utility methods for this purpose.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CalendarEvent  implements CalendarEventService, Comparable<CalendarEvent> {

    /**
     * The name of the event.
     */
    private String name;

    /**
     * The date of the event.
     * This field is annotated with @XmlJavaTypeAdapter to enable XML marshalling and unmarshalling
     * of LocalDate objects. By default, JAXB will treat fields as XML elements, but we want to
     * treat this field as an XML attribute, so we use the XmlJavaTypeAdapter annotation to specify
     * the adapter class to use.
     */
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    /**
     * The start time of the event.
     * This field is annotated with @XmlJavaTypeAdapter to enable XML marshalling and unmarshalling
     * of LocalTime objects. By default, JAXB will treat fields as XML elements, but we want to
     * treat this field as an XML element, so we use the XmlJavaTypeAdapter annotation to specify
     * the adapter class to use.
     */
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime startTime;

    /**
     * The end time of the event.
     * This field is annotated with @XmlJavaTypeAdapter to enable XML marshalling and unmarshalling
     * of LocalTime objects. By default, JAXB will treat fields as XML elements, but we want to
     * treat this field as an XML element, so we use the XmlJavaTypeAdapter annotation to specify
     * the adapter class to use.
     */
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime endTime;

    /**
     * Additional notes about the event.
     */
    private String note;

    /**
     * Whether the event is a holiday.
     * This field is annotated with @XmlAttribute to indicate that it should be treated as an
     * XML attribute rather than an XML element.
     */
    @XmlAttribute
    private boolean isHoliday;


    /**
     * Constants used throughout the class.
     * These constants define the patterns used to format dates and times, as well as the
     * corresponding DateTimeFormatter objects used to parse and format them.
     */
    //region Constants

    public static final String DATE_PATTERN="dd-MM-yyyy";

    public static final String TIME_PATTERN="HH:mm";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    //endregion

    /**
     * Default constructor.
     * Creates a new CalendarEvent object with default values for all fields.
     * JAXB marshal and unmarshal method requires default constructor for the element class.
     */
    public CalendarEvent(){}

    /**
     * Constructor with event name, date, start time, end time, and note parameters
     * @param eventName name of the event
     * @param date date of the event
     * @param startTime start time of the event
     * @param endTime end time of the event
     * @param note note for the event
     * @throws InvalidTimeIntervalException if the start time is after the end time
     * @throws CalendarDateException if the date is invalid
     * @throws CalendarTimeException if the start time or end time is invalid
     */
    public CalendarEvent(String eventName, String date, String startTime, String endTime, String note) throws InvalidTimeIntervalException, CalendarDateException, CalendarTimeException {

        try {
            this.date = LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e) {
            throw new CalendarDateException("Invalid date format. Please use "+ DATE_PATTERN);
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


    /**
     * Constructs a new CalendarEvent object with the specified event name, date, start and end times, and note.
     * @param eventName the name of the event
     * @param date the date of the event
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * @param note a note associated with the event
     * @throws InvalidTimeIntervalException if the start time is after the end time
     * @throws CalendarDateException if the provided date is not a valid date
     * @throws CalendarTimeException if either the start time or end time is not a valid time
     */
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


    /**
     * Creates a new CalendarEvent object with the given date and start time.
     * @param date a string representing the date of the event in the format of "dd-MM-yyyy"
     * @param startTime a string representing the start time of the event in the format of "HH:mm"
     * @throws CalendarDateException if the provided date is invalid or not a leap year
     * @throws CalendarTimeException if the provided start time is invalid
     */
    public CalendarEvent(String date, String startTime) throws CalendarDateException, CalendarTimeException {
        try{
            this.date = LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarDateException("Invalid date format. Please use "+ DATE_PATTERN);
        }

        if(!this.date.isLeapYear())
        {
            if(this.date.getDayOfMonth()==29)
            {
                throw new CalendarDateException(this.date + " is not a leap year!");
            }
        }

        try {
            this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarTimeException();
        }

    }

    /**
     * Creates a new CalendarEvent object with the given date.
     * @param date a string representing the date of the event in the format of "dd-MM-yyyy"
     * @throws CalendarDateException if the provided date is invalid or not a leap year
     */
    public CalendarEvent(String date) throws CalendarDateException {
        try{
            this.date = LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarDateException("Invalid date format. Please use "+ DATE_PATTERN);
        }
    }


    /**
     * This method overrides the equals() method of the Object class to check whether this CalendarEvent object is equal to the given object.
     * Two CalendarEvent objects are considered equal if they have the same date, and either the startTime or endTime is the same.
     * @param o the object to be compared with this CalendarEvent object
     * @return true if the two objects are equal, false otherwise
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent event = (CalendarEvent) o;
        return date.equals(event.date) && (startTime.equals(event.startTime)||endTime.equals(event.endTime));
    }

    /**
     * This method overrides the hashCode() method of the Object class to generate a unique hash code for this CalendarEvent object.
     * The hash code is generated based on the date, startTime and endTime of the event.
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }

    /**
     * This method overrides the compareTo() method of the Comparable interface to compare two CalendarEvent objects based on their startTime.
     * @param o the CalendarEvent object to be compared with this object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(CalendarEvent o) {
        return this.startTime.compareTo(o.startTime);
    }

    /**
     * This method overrides the toString() method of the Object class to return a string representation of this CalendarEvent object.
     * If the event is on a weekend, it is labeled as a holiday in the string representation.
     * The string representation consists of the date, startTime, endTime, name and note of the event.
     * @return a string representation of this CalendarEvent object
     */
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

    /**
     * This method checks whether the given CalendarEvent object is compatible with this CalendarEvent object.
     * Two events are considered incompatible if they occur on the same day and there is an overlap in their time intervals.
     * @param calendarEvent the CalendarEvent object to be checked for compatibility
     * @return true if the two events are compatible, false otherwise
     */
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
    /**
     * Returns the name of the event
     * @return a string representing the event name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event
     * @param eventName a string representing the event name
     */
    public void setName(String eventName) {
        this.name = eventName;
    }

    /**
     * Returns the date of the event
     * @return a LocalDate object representing the event date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the event
     * @param date a LocalDate object representing the event date
     * @throws InvalidTimeIntervalException if the new date is invalid based on the event's start and end times
     */
    public void setDate(LocalDate date) throws InvalidTimeIntervalException {
        this.date = date;
        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }
    }

    /**
     * Returns the start time of the event
     * @return a LocalTime object representing the start time of the event
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event
     * @param startTime a LocalTime object representing the start time of the event
     * @throws InvalidTimeIntervalException if the new start time is after the current end time of the event
     */
    public void setStartTime(LocalTime startTime) throws InvalidTimeIntervalException {
        this.startTime = startTime;
        if(this.startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }
    }

    /**
     * Returns the end time of the event
     * @return a LocalTime object representing the end time of the event
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event
     * @param endTime a LocalTime object representing the end time of the event
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the note associated with the event
     * @return a string representing the note of the event
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the note of the event
     * @param note a string representing the note of the event
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Returns a boolean indicating whether the event is a holiday or not
     * @return true if the event is a holiday, false otherwise
     */
    public boolean isHoliday() {
        return isHoliday;
    }

    /**
     * Sets a boolean indicating whether the event is a holiday or not
     * @param holiday true if the event is a holiday, false otherwise
     */
    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    //endregion
}

