package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.time.LocalDate;
import java.util.ArrayList;

import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;

//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;

public class Holiday implements Operation {

    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //Constructors~~~~~~~~~~~~~~~~~~~~~~
    public Holiday(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        //try {
            //Date date= DATE_FORMAT.parse(instructions.get(0));
            LocalDate date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);

            for(CalendarEvent event:fileParser.getFileContent()){
                if(event.getDate().equals(date)){
                    event.setHoliday(true);
                }
            }

            return true;
        //} catch (ParseException e) {
        //    System.out.println("Please input correct data format: "+ DATE_FORMAT);
        //}
    }
}
