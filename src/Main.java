import models.calendar.Calendar;
import models.calendar.CalendarEvent;
import models.cli.CalendarCLI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The entry point of the calendar program.
 */
public class Main {
    /**
     * The main method that starts the calendar program by calling the CalendarCLI run method.
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {
        CalendarCLI.run();
    }
}