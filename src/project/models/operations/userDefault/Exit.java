package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.models.calendar.CalendarService;
import project.models.parsers.XMLParser;

/**
 * A class representing the "Exit" command, which is used to exit the program.
 */
public class Exit implements DefaultOperation {

    /**
     * Calendar service object contains file info and calendar repository.
     */
    private CalendarService calendarService;

    /**
     * Constructs a Close object with the provided {@link CalendarService} object.
     * @param calendarService The CalendarService object that will be used to parse the calendar file.
     */
    public Exit(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Executes the "Exit" command by printing a message indicating that the program is exiting.
     */
    @Override
    public void execute() {
        calendarService.setRepository(null);
        calendarService.setLoadedFile(null);
        calendarService=null;
        System.gc();
        System.exit(0);
    }
}
