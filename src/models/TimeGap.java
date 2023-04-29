package models;


import java.time.LocalTime;

/**
 * The TimeGap class represents a time interval between two LocalTime instances, consisting of a start time and an end time.
 */
public class TimeGap {

    /**
     * The start time of the time interval represented by this TimeGap instance.
     */
    private LocalTime startTime;

    /**
     * The end time of the time interval represented by this TimeGap instance.
     */
    private LocalTime endTime;

    /**
     * Constructs a new TimeGap instance with the specified start and end times.
     * @param startTime the start time of the time interval.
     * @param endTime the end time of the time interval.
     */
    public TimeGap(LocalTime startTime, LocalTime endTime){
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
     */
    public void setStartTime(LocalTime startTime) {
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
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    //endregion
}
