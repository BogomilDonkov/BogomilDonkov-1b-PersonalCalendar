package adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Custom XmlAdapter for marshaling and unmarshalling LocalDate objects.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    /**
     * Formatter for LocalDate string representation.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Converts a string representation of LocalDate to LocalDate object.
     * @param v String representation of LocalDate to be converted.
     * @return LocalDate object corresponding to the input string representation.
     */
    @Override
    public LocalDate unmarshal(String v){
        return LocalDate.parse(v, formatter);
    }

    /**
     * Converts a LocalDate object to its string representation.
     * @param v LocalDate object to be converted.
     * @return String representation of the input LocalDate object.
     */
    @Override
    public String marshal(LocalDate v){
        return v.format(formatter);
    }
}
