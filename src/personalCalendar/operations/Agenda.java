package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;
//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;

public class Agenda implements Operation {

    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //region Constructors
    public Agenda(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //endregion

    //region Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        //try {
        //Date date=DATE_FORMAT.parse(instructions.get(0));


        LocalDate date = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        ArrayList<CalendarEvent> calendarEvents=new ArrayList<>(fileParser.getFileContent());

        if(calendarEvents.isEmpty())
        {
            System.out.println("There are no events within the current set date: "+ DATE_FORMATTER/*DATE_FORMAT.format(date)*/);
            return false;
        }

        Collections.sort(calendarEvents);
        System.out.println("Date\t\t\tStartTime\tEndTime\t\tName\t\t\tNote");

        for(CalendarEvent event:calendarEvents){
            if(event.getDate().equals(date)){
                System.out.println(event);
            }
        }

        //} catch (ParseException e) {
        //System.out.println("Please input correct date format "+dateFormatter);
        //}
        return true;
    }
    //endregion
}
