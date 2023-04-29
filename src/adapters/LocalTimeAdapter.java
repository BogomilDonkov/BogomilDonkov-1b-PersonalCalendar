package adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String,LocalTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return LocalTime.parse(v, formatter);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return v.format(formatter);
    }
}
