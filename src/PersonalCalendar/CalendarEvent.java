package PersonalCalendar;

import PersonalCalendar.Exceptions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarEvent implements Comparable<CalendarEvent>{
    //Members~~~~~~~~~~~~~~~~~~~~~~~~
    private String name;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String note;

    private boolean isHoliday;

    //Constants~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat TIME_FORMAT=new SimpleDateFormat("HH:mm");

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~
    public CalendarEvent(String name, String date, String startTime, String endTime, String note) throws CalendarDateException, CalendarTimeException, InvalidTimeIntervalException {

        try {
            this.date = DATE_FORMAT.parse(date);

        } catch (ParseException e) {
            throw new CalendarDateException();
        }

        try {
            this.startTime = TIME_FORMAT.parse(startTime);
            this.endTime = TIME_FORMAT.parse(endTime);

        } catch (ParseException e) {
            throw new CalendarTimeException();
        }

        if(this.startTime.after(this.endTime))
            throw new InvalidTimeIntervalException();

        this.name = name;
        this.note = note;
    }

    public CalendarEvent(String date, String startTime) throws CalendarDateException, CalendarTimeException{

        try {
            this.date = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new CalendarDateException();
        }

        try {
            this.startTime = TIME_FORMAT.parse(startTime);
        } catch (ParseException e) {
            throw new CalendarTimeException();
        }
    }

    public CalendarEvent(String date) throws CalendarDateException{
        try {
            this.date = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new CalendarDateException();
        }
    }

    //Operations~~~~~~~~~~~~~~~~~~~~~~~~~

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
            return DATE_FORMAT.format(date) +"\t\t"+ TIME_FORMAT.format(startTime) +"\t\t"+ TIME_FORMAT.format(endTime) +"\t\t"+ name +"\t\t"+ note + "\t\t" +" holiday";

        return DATE_FORMAT.format(date) +"\t\t"+ TIME_FORMAT.format(startTime) +"\t\t"+ TIME_FORMAT.format(endTime) +"\t\t"+ name +"\t\t"+ note + "\t\t" + "work day";
    }

    public boolean checkCompatibility(CalendarEvent calendarEvent){

        if(calendarEvent.getDate().equals(this.date))
        {
            if(calendarEvent.startTime.after(this.startTime)&&calendarEvent.startTime.before(this.endTime))
            {
                return false;
            }

            return !calendarEvent.endTime.after(this.startTime) || !calendarEvent.endTime.before(this.endTime);
        }
        return true;
    }

    //Accessors/Mutators~~~~~~~~~~~~~~~~~~~~~~
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
}
