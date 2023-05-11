package project.models.cli;

import project.contracts.Operation;
import project.models.operations.Commands;
import project.exceptions.CalendarException;
import project.exceptions.OperationException;
import project.models.operations.OperationFactory;
import project.models.calendar.PersonalCalendar;
import project.models.parsers.XMLParser;
import project.util.CalendarScanner;

import java.util.*;


/**
 * The CalendarCLI class represents the command line interface for the calendar application.
 * It provides a run method that continuously prompts the user for input and executes the corresponding operation.
 * The user input is parsed using a scanner and split into an ArrayList of strings, which is then used to create an Operation object.
 * The Operation object is then executed and its result is printed to the console.
 * If the Operation object is an instance of the Exit class, the program exits the while loop and terminates.
 */
public class CalendarCLI {

    /**
     * Private because we don't want to instantiate this class.
     */
    private CalendarCLI(){}

    /**
     * The run method is the main entry point of the CalendarCLI class.
     * It continuously prompts the user for input, creates an Operation object using the input, and executes it by calling its execute method.
     * If the Operation object is an instance of the Exit class, the program terminates.
     */
    public static void run() {
        PersonalCalendar personalCalendar =new PersonalCalendar();
        XMLParser xmlParser=XMLParser.getInstance(personalCalendar);
        OperationFactory operationFactory=new OperationFactory(xmlParser);
        ArrayList<String> inputString;
        ArrayList<String> instructions;

        while(true) {
            System.out.print(">");

            String input= CalendarScanner.scanNextLine();

            if(input.equals(""))
                continue;

            String regex ="\\s+(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)(?=(?:[^']*'[^']*')*[^']*$)";
            inputString = new ArrayList<>(List.of(input.split(regex)));

            if(inputString.isEmpty())
                continue;

            instructions = new ArrayList<>(inputString.subList(1,inputString.size()));

            try {
                Commands command=parseCommand(inputString.get(0));

                checkInstructionsLength(instructions,command);

                Operation operation=operationFactory.getOperation(command,instructions);

                operation.execute();
            } catch (CalendarException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //region Internal Methods
    /**
     * Parses the given command string and returns the corresponding {@link Commands} enum value.
     * Throws an {@link OperationException} if the command is not recognized as an internal command.
     * @param command the command string to parse
     * @return the corresponding {@link Commands} enum value
     * @throws OperationException if the command is not recognized as an internal command
     */
    private static Commands parseCommand(String command) throws OperationException {
        for(Commands value:Commands.values())
            if(value.getName().equals(command))
                return Commands.valueOf(command.toUpperCase());

        throw new OperationException(command + " is not recognized as an internal command!");
    }

    /**
     * Checks if the length of the instructions is correct for the specified command.
     * Book, SaveAs, Merge, Change  and Findslotwith are project.exceptions,
     * because they can take more than their minimum argument size requirement.
     * @param instructions the instructions for the specified command.
     * @param command the specified command.
     * @throws OperationException if the length of the instructions is not correct for the specified command.
     */
    private static void checkInstructionsLength(ArrayList<String> instructions, Commands command) throws OperationException {
        int defaultInstructionsSize=command.getInstructions().split(" ").length;

        switch(command){
            case BOOK:
            case SAVEAS:
            case MERGE:
            case CHANGE:
            case FINDSLOTWITH:
                if(instructions.size() < defaultInstructionsSize) {
                    throw new OperationException("'" + command + "' expects " + command.getInstructions());
                }
                break;

            default:
                if (instructions.size() != defaultInstructionsSize ) {

                if(command.getInstructions().equals(" "))
                    throw new OperationException("'" + command + "' does not expects arguments.");
                else
                    throw new OperationException("'" + command + "' expects "+command.getInstructions());
            }
        }
    }
    //endregion
}
