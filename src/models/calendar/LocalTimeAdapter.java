package models.calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Custom XmlAdapter for marshaling and unmarshalling LocalTime objects.
 */
public class LocalTimeAdapter extends XmlAdapter<String,LocalTime> {

    /**
     * DateTimeFormatter used for parsing and formatting LocalTime objects.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Unmarshalls the specified string into a LocalTime object.
     * @param v the string to unmarshal
     * @return the LocalTime object created from the string
     */
    @Override
    public LocalTime unmarshal(String v) {
        return LocalTime.parse(v, formatter);
    }

    /**
     * Marshals the specified LocalTime object into a string.
     * @param v the LocalTime object to marshal
     * @return the string representation of the LocalTime object
     */
    @Override
    public String marshal(LocalTime v) {
        return v.format(formatter);
    }
}
