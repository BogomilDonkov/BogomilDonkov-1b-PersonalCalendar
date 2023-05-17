package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.models.calendar.CalendarService;
import project.models.parsers.XMLParser;

/**
 * The Close class implements the DefaultOperation interface and represents the operation
 * of closing a calendar file.
 */
public class Close implements DefaultOperation {

    /**
     * Calendar service object contains file info and calendar repository.
     */
    private final CalendarService calendarService;

    /**
     * Constructs a Close object with the provided {@link CalendarService} object.
     * @param calendarService The CalendarService object that will be used to parse the calendar file.
     */
    public Close(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Closes the calendar file associated with the {@link CalendarService} object and sets the calendar and file to null.
     */
    @Override
    public  void execute() {
        System.out.println("File successfully closed "+ calendarService.getLoadedFile().getAbsolutePath());
        calendarService.setRepository(null);
        calendarService.setLoadedFile(null);
    }
}
