package PersonalCalendar;

import PersonalCalendar.Exceptions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CalendarEvent {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~
    private String name;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String note;

    //Constants~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~
    public CalendarEvent(String name, String date, String startTime, String endTime, String note) throws CalendarDateException, CalendarTimeException {

        this.name = name;

        try {
            this.date = dateFormat.parse(date);

        } catch (ParseException e) {
            throw new CalendarDateException();
        }

        try {
            this.startTime = timeFormat.parse(startTime);
            this.endTime = timeFormat.parse(endTime);
        } catch (ParseException e) {
            throw new CalendarTimeException();
        }

        this.note = note;
    }

    //Operations~~~~~~~~~~~~~~~~~~~~~~~~~

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent event = (CalendarEvent) o;
        return date.equals(event.date) && startTime.equals(event.startTime) && endTime.equals(event.endTime);
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
}
