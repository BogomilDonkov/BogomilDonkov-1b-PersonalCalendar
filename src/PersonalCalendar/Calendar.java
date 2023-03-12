package PersonalCalendar;

import CommandLineInterface.*;
import CommandLineInterface.Parsers.*;

import java.util.*;

public class Calendar {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final Scanner scanner=new Scanner(System.in);

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void run(){

        CommandLineHandler commandLineHandler=new CalendarCommandLineHandler(new ArrayList<>(List.of(Commands.values())));
        XMLParser xmlParser=new CalendarXMLParser();
        Operations<FileParser> operations=new Operations<>(xmlParser);



        while(true) {
            System.out.print(">");

            commandLineHandler.handleInput(scanner.nextLine());

            commandLineHandler.handleOperation(operations);
        }
    }
}
