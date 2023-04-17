package personalCalendar;

import cli.enums.DefaultCommands;
import cli.exceptions.OperationException;
import cli.factories.OperationFactory;
import cli.interfaces.Operation;
import cli.parsers.*;
import personalCalendar.enums.CalendarCommands;
import personalCalendar.parsers.CalendarXMLParser;

import java.util.*;

public class CalendarService {
    //region Members
    private static final Scanner scanner=new Scanner(System.in);
    private static final HashSet<String> commands=new HashSet<>();

    //endregion

    //region Methods
    public static void run() {
        XMLParser xmlParser=new CalendarXMLParser();
        OperationFactory operationFactory=new OperationFactory(xmlParser);
        DefaultCommands command;
        ArrayList<String> inputString;
        ArrayList<String> instructions;

        //Зареждане на командите по подразбиране
        loadCommands(DefaultCommands.values());

        //Зареждане на командите на календара
        loadCommands(CalendarCommands.values());

        boolean runningProgram=true;

        while(runningProgram) {
            System.out.print(">");

            String input=scanner.nextLine();

            if(input.equals(""))
                continue;

            inputString = new ArrayList<>(List.of(input.split("\\s+")));

            if(inputString.isEmpty())
                continue;

            //Проверка за наличност на въведената команда
            if(commands.contains(inputString.get(0).toUpperCase()))
                command= DefaultCommands.valueOf(inputString.get(0).toUpperCase());
            else
            {
                System.out.println(inputString.get(0) + " is not recognized as an internal command!");
                continue;
            }

            instructions = new ArrayList<>(inputString.subList(1,inputString.size()));

            try {
                Operation operation=operationFactory.getOperation(command,instructions);
                if(operation!=null)
                    runningProgram=operation.execute();
            } catch (OperationException e) {
                break;
            }
        }
    }
    //endregion

    //region Internal Methods
    private static  <T extends Enum<T>>  void loadCommands(T[] commandList){
        HashSet<String> newCommands=new HashSet<>();

        //Зареждане на командите в полето ни за команди
        for(Enum<T> value: commandList){
            newCommands.add(value.toString());
        }

        if(newCommands.size() != commandList.length)
            System.out.println("""
                    ______________________________________
                    Неуспешно зареждане на всички команди!
                    --------------------------------------""");
        else
            commands.addAll(newCommands);
    }
    //endregion
}
