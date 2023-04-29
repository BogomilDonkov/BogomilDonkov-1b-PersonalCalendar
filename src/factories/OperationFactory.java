package factories;

import contracts.Operation;
import enums.*;
import exceptions.OperationException;
import parsers.XMLParser;
import operations.defaultOp.*;
import operations.calendarOp.*;

import java.util.ArrayList;

/**
 * A factory class for creating operations based on the user input command.
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
    public Operation getOperation(String commandInput, ArrayList<String> instructions) throws OperationException {

        Commands command=parseCommand(commandInput);

        checkInstructionsLength(instructions,command);

        if(checkIfFileIsLoaded()) {
            switch (command) {
                case EXIT -> { return new Exit(); }
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
                case EXIT -> { return new Exit(); }
                case HELP -> { return new Help(); }
                case OPEN -> { return new Open(xmlParser, instructions); }
                default -> throw new OperationException("There is no currently opened file at the moment.");
            }
        }
    }


    //region Internal Methods

    /**
     * Checks whether a file is loaded or not.
     * @return true if a file is loaded, false otherwise.
     */
    private boolean checkIfFileIsLoaded(){
        return xmlParser.getFile() != null;
    }

    /**
     * Checks if the length of the instructions is correct for the specified command.
     * @param instructions the instructions for the specified command.
     * @param command the specified command.
     * @throws OperationException if the length of the instructions is not correct for the specified command.
     */
    private void checkInstructionsLength(ArrayList<String> instructions, Commands command) throws OperationException {
        int defaultInstructionsSize=command.getInstructions().split(" ").length;

        //Имаме едно единствено изключение ако командата ни е BOOK
        //Тъй като note може да съдържа в себе си повече от една дума, дължината на подадените инструкции може да надвишава зададената дължина по подразбиране
        if(command== Commands.BOOK) {
            if(instructions.size() < defaultInstructionsSize) {
                throw new OperationException("'" + command + "' expects " + command.getInstructions());
            }
        }
        else
        //Проверяваме дължината на подадените инструкции дали съвпада с дължината на инструкциите които изисква подадената команда
        if (instructions.size() != defaultInstructionsSize ) {

            if(command.getInstructions().equals(" "))
                throw new OperationException("'" + command + "' does not expects arguments.");
            else
                throw new OperationException("'" + command + "' expects "+command.getInstructions());
        }
    }

    /**
     * Parses the given command string and returns the corresponding {@link Commands} enum value.
     * Throws an {@link OperationException} if the command is not recognized as an internal command.
     * @param command the command string to parse
     * @return the corresponding {@link Commands} enum value
     * @throws OperationException if the command is not recognized as an internal command
     */
    private Commands parseCommand(String command) throws OperationException {
        for(Commands value:Commands.values())
            if(value.getName().equals(command))
                return Commands.valueOf(command.toUpperCase());

        throw new OperationException(command + " is not recognized as an internal command!");
    }

    //endregion
}
