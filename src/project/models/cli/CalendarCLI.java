package project.models.cli;

import project.contracts.CommandLineInterface;
import project.contracts.Operation;
import project.models.calendar.CalendarService;
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
public class CalendarCLI implements CommandLineInterface<Commands> {

    private PersonalCalendar personalCalendar;
    private XMLParser xmlParser;
    private CalendarService calendarService;
    private OperationFactory operationFactory;
    private List<String> inputString;
    private List<String> instructions;

    private static CalendarCLI instance;

    /**
     * Private because we don't want to instantiate this class.
     */
    private CalendarCLI(){
        personalCalendar =new PersonalCalendar();
        xmlParser=new XMLParser();
        calendarService=new CalendarService(personalCalendar,xmlParser);
        operationFactory=new OperationFactory(calendarService);
        inputString=new ArrayList<>();
        instructions=new ArrayList<>();
    }


    public static CalendarCLI getInstance(){
        if(instance==null){
            instance=new CalendarCLI();
        }
        return instance;
    }

    /**
     * The run method is the main entry point of the CalendarCLI class.
     * It continuously prompts the user for input, creates an Operation object using the input, and executes it by calling its execute method.
     * If the Operation object is an instance of the Exit class, the program terminates.
     */
    public void run() {
        System.out.println("~ CALENDAR APPLICATION ~\n");

        while(true) {
            System.out.print(">");

            String input= CalendarScanner.scanNextLine();

            if(!processCommand(input)){
                continue;
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
    private Commands parseCommand(String command) throws OperationException {
        for(Commands value:Commands.values())
            if(value.getName().equals(command))
                return Commands.valueOf(command.toUpperCase());

        throw new OperationException(command + " is not recognized as an internal command!");
    }

    /**
     Checks the validity of the input command.
     @param input the input command to be checked
     @return {@code true} if the input command is valid, {@code false} otherwise
     */
    private boolean checkInput(String input){
        if(input.equals(""))
            return false;

        String regex="\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        inputString = Arrays.asList(input.split(regex));

        if(inputString.isEmpty())
            return false;

        instructions = new ArrayList<>(inputString.subList(1,inputString.size()));

        return true;
    }

    /**
     Processes the given input command.
     @param input the input command to be processed
     @return {@code true} if the command was successfully processed, {@code false} otherwise
     */
    private boolean processCommand(String input) {

        if(!checkInput(input)){
            return false;
        }

        try {
            Commands command=parseCommand(inputString.get(0));

            checkInstructionsLength(instructions,command);

            Operation operation=operationFactory.getOperation(command,instructions);

            operation.execute();
        } catch (CalendarException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * Checks if the length of the instructions is correct for the specified command.
     * Book, SaveAs, Merge, Change  and Findslotwith are project.exceptions,
     * because they can take more than their minimum argument size requirement.
     * @param instructions the instructions for the specified command.
     * @param command the specified command.
     * @throws OperationException if the length of the instructions is not correct for the specified command.
     */
    private void checkInstructionsLength(List<String> instructions, Commands command) throws OperationException {
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
