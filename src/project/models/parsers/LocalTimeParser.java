package project.models.parsers;



import project.exceptions.CalendarTimeException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A utility class for parsing and formatting LocalTime objects.
 */
public final class LocalTimeParser {

    /**
     * Time format pattern.
     */
    private static final String TIME_PATTERN="HH:mm";

    /**
     * The {@link DateTimeFormatter} used to parse and format {@link LocalTime} objects.
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /**
     * Parses a string representing a time in the format "HH:mm" into a {@link LocalTime} object.
     * @param time the string representing the time to parse
     * @return a LocalTime object representing the parsed time
     * @throws CalendarTimeException if the string cannot be parsed into a {@link LocalTime} object
     */
    public static LocalTime parse(String time) throws CalendarTimeException {
        try {
            LocalTime parsedTime = LocalTime.parse(time, TIME_FORMATTER);
            return parsedTime;
        } catch (DateTimeParseException ignored) {
            throw new CalendarTimeException("Invalid time format. Please use "+ TIME_PATTERN);
        }
    }

    /**
     * Formats a {@link LocalTime} object as a string in the format "HH:mm".
     * @param time the {@link LocalTime} object to format.
     * @return a string representing the formatted {@link LocalTime} object.
     * @throws DateTimeParseException if the {@link LocalTime} object cannot be formatted.
     */
    public static String format(LocalTime time) throws DateTimeParseException{
        return TIME_FORMATTER.format(time);
    }
}
