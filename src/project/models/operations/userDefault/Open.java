package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.exceptions.OperationException;
import project.models.calendar.CalendarService;
import project.models.calendar.PersonalCalendar;
import project.models.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Open class represents an operation for opening an XML file containing a calendar.
 */
public class Open implements DefaultOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final CalendarService calendarService;

    /**
     * Name of the file to open.
     */
    private String fileDirectory;

    /**
     * Constructs a Open object with the provided CalendarService and instruction list.
     * @param calendarService The CalendarService object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Open(CalendarService calendarService, List<String> instructions) {
        this.calendarService = calendarService;
        this.fileDirectory = instructions.get(0);
    }

    /**
     * Executes the Open operation, opening the XML file containing the calendar.
     * @throws OperationException If an error occurs while opening the file.
     */
    @Override
    public void execute() throws OperationException {

        if(!fileDirectory.endsWith(".xml")) {
            fileDirectory += ".xml";
        }

        calendarService.setLoadedFile(new File(fileDirectory));
        calendarService.setRepository(new PersonalCalendar());

        if(calendarService.getLoadedFile().exists()) {

            calendarService.importToRepository();

            System.out.println("File successfully opened: " + fileDirectory);

            if (calendarService.getRepository().isEmpty()) {
                System.out.println("File is empty.");
            }

        }else {

            System.out.println("File not found.");
            calendarService.createFileIfNotExist();
            System.out.println("New file was created and loaded: " + fileDirectory);
        }
    }
}
