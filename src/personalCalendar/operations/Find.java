package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.util.ArrayList;
import java.util.HashSet;

public class Find implements Operation {
    //region Members~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion


    //region Constructors~~~~~~~~~~~~~~~~~~~~~~
    public Find(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //endregion

    //region Methods~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        HashSet<CalendarEvent> foundedEvents=new HashSet<>();

        StringBuilder string=new StringBuilder();
        for(String value:instructions)
            string.append(value);

        for(CalendarEvent event:fileParser.getFileContent()){
            if(event.getName().contains(string)||event.getNote().contains(string)){
                foundedEvents.add(event);
            }
        }

        if(!foundedEvents.isEmpty()){
            System.out.println("Here are the events that contain "+string);
            for(CalendarEvent event:foundedEvents){
                System.out.println(event);
            }
            return true;
        }
        else
            System.out.println("There are no events that contain: "+string);

        return false;
    }
    //endregion
}
