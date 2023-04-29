package models;

import java.time.LocalTime;

public class TimeGap {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeGap(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "From " + startTime + " to " + endTime;
    }


    //region Getters and Setters

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

    //endregion
}
