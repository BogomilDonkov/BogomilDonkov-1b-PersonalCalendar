package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.exceptions.OperationException;
import project.models.calendar.CalendarService;
import project.models.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;

/**
 * Represents a command to save the current state of the calendar to the file system.
 * Implements the DefaultOperation interface, so it can be executed by the main program.
 */
public class Save implements DefaultOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final CalendarService calendarService;

    /**
     * The ArrayList containing the instructions for the operation.
     * @param calendarService The CalendarService object that will be used to parse the calendar.
     */
    public Save(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Executes the Save command. Writes the current state of the calendar to a file using the XMLParser from the CalendarService.
     * Deletes any merged calendars that were previously created. If an error occurs during writing, an OperationException is thrown.
     * @throws OperationException If an error occurs during writing.
     */
    @Override
    public void execute() throws OperationException {
        calendarService.exportFromRepository();

        System.out.println("File successfully saved "+ calendarService.getLoadedFile().getAbsolutePath());
    }
}
