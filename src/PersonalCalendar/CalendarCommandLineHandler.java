package PersonalCalendar;

import CommandLineInterface.CommandLineHandler;
import CommandLineInterface.Commands;
import CommandLineInterface.Operations;

import java.util.ArrayList;

public class CalendarCommandLineHandler extends CommandLineHandler {


    public CalendarCommandLineHandler(ArrayList<Commands> commands) {
        super(commands);
    }


   public void handleOperation(CalendarOperations operations){
       String command= super.getCommand().toString();

        switch (command) {
           case "close" -> {
               if (getInstructions().size() != 0)
                   System.out.println("'close' does not expect arguments");
               else
                   operations.close();
           }

           case "open" -> {
               if (getInstructions().size() != 1)
                   System.out.println("'open' expects one argument");
               else
                   operations.open(getInstructions().get(0));
           }

           case "help" -> {
               if (getInstructions().size() != 0)
                   System.out.println("'help' does not expect arguments");
               else
                   System.out.println(operations.help());
           }

           case "save" -> {
               if (getInstructions().size() != 0)
                   System.out.println("'save' does not expect arguments");
               else
                   operations.save();
           }

           case "saveas" -> {
               if (getInstructions().size() != 1)
                   System.out.println("'saveas' expects one argument");
               else
                   operations.saveAs(getInstructions().get(0));
           }

           case "exit" ->{
               if (getInstructions().size() != 0)
                   System.out.println("'exit' does not expect arguments");
               else
                   operations.exit();
           }

           case "book" ->{
               operations.book(getInstructions());
           }

           default -> System.out.println(getCommand()+" is not recognized as an internal command!");
       }
   }

}
