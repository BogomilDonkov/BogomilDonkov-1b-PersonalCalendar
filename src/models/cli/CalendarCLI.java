package models.cli;

import contracts.Operation;
import enums.Commands;
import exceptions.OperationException;
import factories.OperationFactory;
import models.calendar.Calendar;
import models.operations.userDefault.Exit;
import parsers.XMLParser;

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
     * A static final Scanner object used to read input from the console.
     */
    public static final Scanner scanner=new Scanner(System.in);

    /**
     * The run method is the main entry point of the CalendarCLI class.
     * It continuously prompts the user for input, creates an Operation object using the input, and executes it by calling its execute method.
     * If the Operation object is an instance of the Exit class, the program terminates.
     */
    public static void run() {
        Calendar calendar=new Calendar();
        XMLParser xmlParser=new XMLParser(calendar);
        OperationFactory operationFactory=new OperationFactory(xmlParser);
        ArrayList<String> inputString;
        ArrayList<String> instructions;

        while(true) {
            System.out.print(">");

            String input=scanner.nextLine();

            if(input.equals(""))
                continue;

            inputString = new ArrayList<>(List.of(input.split("\\s+")));

            if(inputString.isEmpty())
                continue;

            instructions = new ArrayList<>(inputString.subList(1,inputString.size()));

            try {
                Commands command=parseCommand(inputString.get(0));

                checkInstructionsLength(instructions,command);

                Operation operation=operationFactory.getOperation(command,instructions);

                operation.execute();

                if(operation instanceof Exit)
                    break;

            } catch (OperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

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
     * @param instructions the instructions for the specified command.
     * @param command the specified command.
     * @throws OperationException if the length of the instructions is not correct for the specified command.
     */
    private static void checkInstructionsLength(ArrayList<String> instructions, Commands command) throws OperationException {
        int defaultInstructionsSize=command.getInstructions().split(" ").length;

        if(command== Commands.BOOK) {
            if(instructions.size() < defaultInstructionsSize) {
                throw new OperationException("'" + command + "' expects " + command.getInstructions());
            }
        }
        else

        if (instructions.size() != defaultInstructionsSize ) {

            if(command.getInstructions().equals(" "))
                throw new OperationException("'" + command + "' does not expects arguments.");
            else
                throw new OperationException("'" + command + "' expects "+command.getInstructions());
        }
    }
}
