package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.exceptions.OperationException;
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
    private final XMLParser xmlParser;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    public Save(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    /**
     * Executes the Save command. Writes the current state of the calendar to a file using the XML parser.
     * Deletes any merged calendars that were previously created. If an error occurs during writing, an OperationException is thrown.
     * @throws OperationException If an error occurs during writing.
     */
    @Override
    public void execute() throws OperationException {
        xmlParser.writeFile();

        ArrayList<String> mergedCalendars=xmlParser.getCalendar().getMergedCalendars();

        if(!mergedCalendars.isEmpty())
            for(String calendarName:mergedCalendars)
                xmlParser.deleteFile(calendarName);


        System.out.println("File successfully saved "+ xmlParser.getFile().getAbsolutePath());
    }
}
