package project.models.parsers;

import project.exceptions.CalendarDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * A utility class for parsing and formatting LocalDate objects.
 */
public final class LocalDateParser {

    /**
     * Time format pattern.
     */
    private static final String DATE_PATTERN="dd-MM-yyyy";

    /**
     * The DateTimeFormatter used to parse and format LocalDate objects.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Parses a string representing a date in the format "dd-MM-yyyy" into a {@link LocalDate} object.
     * @param date the string representing the date to parse
     * @return a {@link LocalDate} object representing the parsed date
     * @throws CalendarDateException if the string cannot be parsed into a {@link LocalDate} object
     */
    public static LocalDate parse(String date) throws CalendarDateException {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMATTER);
            return parsedDate;
        } catch (DateTimeParseException e) {
            throw new CalendarDateException("Invalid date format. Please use " + DATE_PATTERN);
        }
    }

    /**
     * Formats a {@link LocalDate} object as a string in the format "dd-MM-yyyy".
     * @param date the {@link LocalDate} object to format
     * @return a string representing the formatted LocalDate object
     * @throws DateTimeParseException if the {@link LocalDate} object cannot be formatted
     */
    public static String format(LocalDate date) throws DateTimeParseException {
        return DATE_FORMATTER.format(date);
    }
}
