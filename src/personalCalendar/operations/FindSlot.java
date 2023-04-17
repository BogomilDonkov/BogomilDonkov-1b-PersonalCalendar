package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;
import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;

public class FindSlot implements Operation {
    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //region Constructors
    public FindSlot(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //endregion

    //region Methods
    @Override
    public boolean execute() {
        //try {
            //Date date = DATE_FORMAT.parse(instructions.get(0));
            LocalDate date=LocalDate.parse(instructions.get(0), DATE_FORMATTER);

            int hours= Integer.parseInt(instructions.get(1));
            ArrayList<CalendarEvent> events=new ArrayList<>();

            for(CalendarEvent event:fileParser.getFileContent()){
                if(event.getDate().equals(date)){
                    //long diff=(event.getEndTime().getTime()-event.getStartTime().getTime());
                    //diff/=3600000;
                    //if(diff==hours){
                    //    events.add(event);
                    //}

                    if(event.getStartTime().until(event.getEndTime(), ChronoUnit.HOURS)==hours){
                        events.add(event);
                    }
                }
            }

            for(CalendarEvent event:events){
                System.out.println(event);
            }

            return true;

        //} catch (ParseException e) {
        //    System.out.println("Please input correct data format: "+ dateFormatter);
        //}
    }
    //endregion
}
