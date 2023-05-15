package project.models.operations;

import project.contracts.Operation;
import project.exceptions.CalendarException;
import project.exceptions.OperationException;
import project.models.operations.manipulations.*;
import project.models.parsers.XMLParser;
import project.models.operations.userDefault.*;
import project.models.operations.inqueries.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory class for creating project.models.operations based on the user input command.
 */
public class OperationFactory{

    /**
     * The XMLParser object used for parsing the XML file.
     */
    private final XMLParser xmlParser;

    /**
     * Constructor for the OperationFactory class.
     * @param xmlParser the XMLParser object used for parsing the XML file.
     */
    public OperationFactory(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    /**
     * Returns the operation based on the user input command.
     * @param commandInput the user input command.
     * @param instructions the instructions that are required for the specified command.
     * @return the operation based on the user input command.
     * @throws OperationException if the command is not recognized or if the instructions are not correct for the specified command.
     */
    public Operation getOperation(Commands command, List<String> instructions) throws CalendarException {

        if(checkIfFileIsLoaded()) {
            switch (command) {
                case EXIT -> { return new Exit(xmlParser); }
                case HELP -> { return new Help(); }
                case CLOSE -> { return new Close(xmlParser); }
                case SAVE -> { return new Save(xmlParser); }
                case SAVEAS -> { return new SaveAs(xmlParser, instructions); }
                case OPEN -> throw new OperationException("There is currently opened file:" + xmlParser.getFile().getAbsolutePath());
                case BOOK -> { return new Book(xmlParser.getCalendar(), instructions); }
                case UNBOOK -> { return new Unbook(xmlParser.getCalendar(), instructions); }
                case AGENDA -> { return new Agenda(xmlParser.getCalendar(), instructions); }
                case CHANGE -> { return new Change(xmlParser.getCalendar(), instructions); }
                case FIND -> { return new Find(xmlParser.getCalendar(), instructions); }
                case HOLIDAY -> { return new Holiday(xmlParser.getCalendar(), instructions); }
                case BUSYDAYS -> { return new Busydays(xmlParser.getCalendar(), instructions); }
                case FINDSLOT -> { return new FindSlot(xmlParser.getCalendar(), instructions); }
                case FINDSLOTWITH -> { return new FindSlotWith(xmlParser, instructions); }
                case MERGE -> { return new Merge(xmlParser, instructions); }
                default -> throw new OperationException("Current operation not found!");
            }
        }
        else {
            switch (command) {
                case EXIT -> { return new Exit(xmlParser); }
                case HELP -> { return new Help(); }
                case OPEN -> { return new Open(xmlParser, instructions); }
                default -> throw new OperationException("There is no currently opened file at the moment.");
            }
        }
    }


    /**
     * Checks whether a file is loaded or not.
     * @return true if a file is loaded, false otherwise.
     */
    private boolean checkIfFileIsLoaded(){
        return xmlParser.getFile() != null;
    }

}
