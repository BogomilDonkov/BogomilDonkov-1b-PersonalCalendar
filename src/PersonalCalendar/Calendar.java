package PersonalCalendar;

import CommandLineInterface.Operations;
import PersonalCalendar.Exceptions.CalendarDateException;
import PersonalCalendar.Exceptions.CalendarTimeException;

import java.util.Scanner;

public class Calendar {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final Scanner scanner=new Scanner(System.in);



    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void run() throws CalendarDateException, CalendarTimeException {

        while(true){
            System.out.print(">");

            String inputString = scanner.nextLine();

        }







    }
}
