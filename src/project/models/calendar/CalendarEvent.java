package project.models.calendar;

import project.contracts.CalendarEventService;
import project.exceptions.CalendarDateException;
import project.exceptions.CalendarException;
import project.exceptions.CalendarTimeException;
import project.exceptions.InvalidTimeIntervalException;
import project.models.parsers.LocalDateParser;
import project.models.parsers.LocalTimeParser;
import project.util.adapters.LocalDateAdapter;
import project.util.adapters.LocalTimeAdapter;

import javax.xml.bind.annotation.*;
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
    @XmlElement
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
     * Represents the start and end time of the event.
     * This field is annotated with @XmlTransient to disable XML marshalling and unmarshalling.
     * Because we want single objects of start and end time itsel we are using apropriete getters.
     */
    @XmlTransient
    private TimeInterval timeInterval;

    /**
     * Additional notes about the event.
     */
    @XmlElement
    private String note;

    /**
     * Whether the event is a holiday.
     * This field is annotated with @XmlAttribute to indicate that it should be treated as an
     * XML attribute rather than an XML element.
     */
    @XmlAttribute
    private boolean isHoliday;

    /**
     * Default constructor.
     * Creates a new CalendarEvent object with default values for all fields.
     * JAXB marshal and unmarshal method requires default constructor for the element class.
     */
    public CalendarEvent() {
        try {
            timeInterval = new TimeInterval(LocalTime.MIN, LocalTime.MAX);
        } catch (InvalidTimeIntervalException ignored){}
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
    public CalendarEvent(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, String note) throws InvalidTimeIntervalException, CalendarDateException {

        if(!date.isLeapYear())
        {
            if(date.getDayOfMonth()==29)
            {
                throw new CalendarDateException(date + " is not a leap year!");
            }
        }

        this.date = date;
        timeInterval =new TimeInterval(startTime,endTime);
        this.name=eventName;
        this.note=note;

        if(date.getDayOfWeek()== DayOfWeek.SATURDAY||date.getDayOfWeek()==DayOfWeek.SUNDAY)
            this.isHoliday=true;
    }


    /**
     * Creates a new CalendarEvent object with the given date and start time.
     * @param date a string representing the date of the event in the format of "dd-MM-yyyy"
     * @param startTime a string representing the start time of the event in the format of "HH:mm"
     * @throws CalendarDateException if the provided date is invalid or not a leap year
     * @throws CalendarTimeException if the provided start time is invalid
     */
    public CalendarEvent(LocalDate date, LocalTime startTime) throws CalendarDateException,InvalidTimeIntervalException {

        if(!date.isLeapYear())
        {
            if(date.getDayOfMonth()==29)
            {
                throw new CalendarDateException(date + " is not a leap year!");
            }
        }

        this.date=date;
        timeInterval =new TimeInterval(startTime,LocalTime.MAX);

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
        return date.equals(event.date) && (getStartTime().equals(event.getStartTime())||getEndTime().equals(event.getEndTime()));
    }

    /**
     * This method overrides the hashCode() method of the Object class to generate a unique hash code for this CalendarEvent object.
     * The hash code is generated based on the date, startTime and endTime of the event.
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, getStartTime(), getEndTime());
    }

    /**
     * This method overrides the compareTo() method of the Comparable interface to compare two CalendarEvent objects based on their startTime.
     * @param o the CalendarEvent object to be compared with this object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(CalendarEvent o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }

    /**
     * This method overrides the toString() method of the Object class to return a string representation of this CalendarEvent object.
     * If the event is on a weekend, it is labeled as a holiday in the string representation.
     * The string representation consists of the date, startTime, endTime, name and note of the event.
     * @return a string representation of this CalendarEvent object
     */
    @Override
    public String toString() {
        String value;
        if(isHoliday)
            value=" holiday";
        else
            value=" work day";


        return  String.format("%-25s", LocalDateParser.format(date) + value)+
                String.format("%-15s", LocalTimeParser.format(getStartTime()))+
                String.format("%-15s", LocalTimeParser.format(getEndTime()))+
                String.format("%-30s", name)+
                String.format("%-40s", note);
    }

    /**
     * This method checks whether the given CalendarEvent object is compatible with this CalendarEvent object.
     * Two events are considered incompatible if they occur on the same day and there is an overlap in their time intervals.
     * @param calendarEvent the CalendarEvent object to be checked for compatibility
     * @return true if the two events are compatible, false otherwise
     */
    @Override
    public boolean checkCompatibility(CalendarEvent calendarEvent){
        if(this.equals(calendarEvent))
            return false;

        if(calendarEvent.date.equals(this.date))
        {
            if(calendarEvent.getStartTime().isAfter(this.getStartTime())&&calendarEvent.getStartTime().isBefore(this.getEndTime()))
                return false;

            if(calendarEvent.getEndTime().isAfter(this.getStartTime())&&calendarEvent.getEndTime().isBefore(this.getEndTime()))
                return false;

            if(this.getStartTime().isAfter(calendarEvent.getStartTime())&&this.getStartTime().isBefore(calendarEvent.getEndTime()))
                return false;

            return !this.getEndTime().isAfter(calendarEvent.getStartTime()) || !this.getEndTime().isBefore(calendarEvent.getEndTime());
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
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the start time of the event
     * @return a LocalTime object representing the start time of the event
     */
    @XmlElement(name="startTime")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getStartTime() {
        return timeInterval.getStartTime();
    }


    /**
     * Sets the start time of the event
     * @param startTime a LocalTime object representing the start time of the event
     * @throws InvalidTimeIntervalException if the new start time is after the current end time of the event
     */
    public void setStartTime(LocalTime startTime) throws InvalidTimeIntervalException {
        timeInterval.setStartTime(startTime);
    }

    /**
     * Returns the end time of the event
     * @return a LocalTime object representing the end time of the event
     */
    @XmlElement(name="endTime")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getEndTime() {
        return timeInterval.getEndTime();
    }

    /**
     * Sets the end time of the event
     * @param endTime a LocalTime object representing the end time of the event
     */
    public void setEndTime(LocalTime endTime) throws InvalidTimeIntervalException {
        timeInterval.setEndTime(endTime);
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

