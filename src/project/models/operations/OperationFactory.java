package project.models.operations;

import project.contracts.FileParser;
import project.contracts.Operation;
import project.exceptions.CalendarException;
import project.exceptions.OperationException;
import project.models.calendar.CalendarService;
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
    private final CalendarService calendarService;

    /**
     * Constructor for the OperationFactory class.
     * @param calendarService the object used for parsing the XML file.
     */
    public OperationFactory(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Returns the operation based on the user input command.
     * @param command the user input command.
     * @param instructions the instructions that are required for the specified command.
     * @return the operation based on the user input command.
     * @throws CalendarException if the command is not recognized or if the instructions are not correct for the specified command.
     */
    public Operation getOperation(Commands command, List<String> instructions) throws CalendarException {

        if(checkIfFileIsLoaded()) {
            switch (command) {
                case EXIT -> { return new Exit(calendarService); }
                case HELP -> { return new Help(); }
                case CLOSE -> { return new Close(calendarService); }
                case SAVE -> { return new Save(calendarService); }
                case SAVEAS -> { return new SaveAs(calendarService, instructions); }
                case OPEN -> throw new OperationException("There is currently opened file:" + calendarService.getLoadedFile().getAbsolutePath());
                case BOOK -> { return new Book(calendarService.getRepository(), instructions); }
                case UNBOOK -> { return new Unbook(calendarService.getRepository(), instructions); }
                case AGENDA -> { return new Agenda(calendarService.getRepository(), instructions); }
                case CHANGE -> { return new Change(calendarService.getRepository(), instructions); }
                case FIND -> { return new Find(calendarService.getRepository(), instructions); }
                case HOLIDAY -> { return new Holiday(calendarService.getRepository(), instructions); }
                case BUSYDAYS -> { return new Busydays(calendarService.getRepository(), instructions); }
                case FINDSLOT -> { return new FindSlot(calendarService.getRepository(), instructions); }
                case FINDSLOTWITH -> { return new FindSlotWith(calendarService, instructions); }
                case MERGE -> { return new Merge(calendarService, instructions); }
                default -> throw new OperationException("Current operation not found!");
            }
        }
        else {
            switch (command) {
                case EXIT -> { return new Exit(calendarService); }
                case HELP -> { return new Help(); }
                case OPEN -> { return new Open(calendarService, instructions); }
                default -> throw new OperationException("There is no currently opened file at the moment.");
            }
        }
    }


    /**
     * Checks whether a file is loaded or not.
     * @return true if a file is loaded, false otherwise.
     */
    private boolean checkIfFileIsLoaded(){
        return calendarService.getLoadedFile() != null;
    }

}
