package project.models.calendar;


import project.exceptions.CalendarException;
import project.exceptions.CalendarTimeException;
import project.exceptions.InvalidTimeIntervalException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The TimeGap class represents a time interval between two LocalTime instances, consisting of a start time and an end time.
 */
public class TimeInterval {

    /**
     * The start time of the time interval represented by this TimeGap instance.
     */
    private LocalTime startTime;

    /**
     * The end time of the time interval represented by this TimeGap instance.
     */
    private LocalTime endTime;


    /**
     * Default constructor
     */
    public TimeInterval() {}

    /**
     * Constructs a new TimeGap instance with the specified start and end times.
     * @param startTime the start time of the time interval.
     * @param endTime the end time of the time interval.
     * @throws InvalidTimeIntervalException invalid time interval
     */
    public TimeInterval(LocalTime startTime, LocalTime endTime) throws InvalidTimeIntervalException {

        if(startTime.isAfter(endTime)) {
            throw new InvalidTimeIntervalException();
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }


    /**
     * Returns a String representation of this TimeGap instance.
     * @return a String representation of this TimeGap instance.
     */
    @Override
    public String toString() {
        return "From " + startTime + " to " + endTime;
    }


    //region Getters and Setters
    /**
     * Returns the start time of the time interval represented by this TimeGap instance.
     * @return the start time of the time interval.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the time interval represented by this TimeGap instance.
     * @param startTime the new start time.
     * @throws InvalidTimeIntervalException invalid time interval
     */
    public void setStartTime(LocalTime startTime) throws InvalidTimeIntervalException {
        if(startTime.isAfter(this.endTime)) {
            throw new InvalidTimeIntervalException();
        }
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the time interval represented by this TimeGap instance.
     * @return the end time of the time interval.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the time interval represented by this TimeGap instance.
     * @param endTime the new end time.
     * @throws InvalidTimeIntervalException invalid time interval
     */
    public void setEndTime(LocalTime endTime) throws InvalidTimeIntervalException {
        if(this.startTime.isAfter(endTime)) {
            throw new InvalidTimeIntervalException();
        }
        this.endTime = endTime;
    }

    //endregion
}
