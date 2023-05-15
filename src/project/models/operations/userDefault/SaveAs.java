package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.exceptions.OperationException;
import project.models.parsers.XMLParser;
import project.util.CalendarScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The SaveAs class implements the DefaultOperation interface and represents a command to save the current calendar to a new file.
 */
public class SaveAs implements DefaultOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final XMLParser xmlParser;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final List<String> instructions;

    /**
     * Constructs a SaveAs object with the provided XMLParser and instruction list.
     * @param xmlParser The XMLParser object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public SaveAs(XMLParser xmlParser, List<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions=instructions;
    }

    /**
     * Executes the SaveAs operation by writing the current calendar to a new file and deleting the old file (if applicable).
     * @throws OperationException if the file cannot be saved as the new directory or if there is an error deleting the old file
     */
    @Override
    public void execute() throws OperationException {
        String newFileDirectory=instructions.get(0);

        if(!newFileDirectory.endsWith(".xml"))
            newFileDirectory+=".xml";

        if(checkIfFileAlreadyExistsAndSubmitUserResponse(newFileDirectory)) {

            String currentDirectory = xmlParser.getFile().getAbsolutePath();

            xmlParser.setFile(new File(newFileDirectory));

            xmlParser.writeFile();
            xmlParser.deleteFile(currentDirectory);

            ArrayList<String> mergedCalendars = xmlParser.getCalendar().getMergedCalendars();

            if (!mergedCalendars.isEmpty())
                for (String calendarName : mergedCalendars)
                    xmlParser.deleteFile(calendarName);

            System.out.println("File saved as " + xmlParser.getFile().getAbsolutePath());
        }
    }

    /**
     * Checks if the new file directory already exists and prompts the user for confirmation if it does.
     * @param newFileDirectory the directory of the new file
     * @return true if the user confirms to proceed, false otherwise
     */
    private boolean checkIfFileAlreadyExistsAndSubmitUserResponse(String newFileDirectory){
        if(new File(newFileDirectory).exists())
        {
            System.out.println("There is "+ newFileDirectory+ " that currently exists.\n" +
                    "Do you want to replace it ? Press 'N' to cancel or press any other key to proceed.");

            String option= CalendarScanner.scanNextLine();

            return !option.equals("N") && !option.equals("n");
        }
        return true;
    }
}
