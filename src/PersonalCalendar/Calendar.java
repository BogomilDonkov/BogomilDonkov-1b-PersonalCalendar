package PersonalCalendar;

import CommandLineInterface.*;
import CommandLineInterface.Parsers.*;

import java.util.*;

public class Calendar {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final Scanner scanner=new Scanner(System.in);

    private static Calendar instance;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private Calendar() {
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void run(){

        CalendarCommandLineHandler calendarCommandLineHandler=new CalendarCommandLineHandler(new ArrayList<>(List.of(CalendarCommands.values())));
        XMLParser xmlParser=new CalendarXMLParser();
        CalendarOperations operations=new CalendarOperations(xmlParser);

        while(true) {
            System.out.print(">");

            calendarCommandLineHandler.handleInput(scanner.nextLine());

            calendarCommandLineHandler.handleOperation(operations);
        }
    }

    private static Calendar getInstance(){
        if(instance==null)
        {
            instance=new Calendar();
            run();
        }

        return instance;
    }

}
