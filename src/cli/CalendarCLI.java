package cli;

import contracts.Operation;
import exceptions.OperationException;
import factories.OperationFactory;
import operations.defaultOp.Exit;
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
        XMLParser xmlParser=new XMLParser();
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
                Operation operation=operationFactory.getOperation(inputString.get(0),instructions);

                operation.execute();

                if(operation instanceof Exit)
                    break;

            } catch (OperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
