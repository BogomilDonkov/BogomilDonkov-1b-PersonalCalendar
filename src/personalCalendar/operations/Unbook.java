package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;
import personalCalendar.exceptions.CalendarDateException;
import personalCalendar.exceptions.CalendarTimeException;
import personalCalendar.exceptions.InvalidTimeIntervalException;

import java.util.ArrayList;

import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;
import static personalCalendar.models.CalendarEvent.TIME_FORMATTER;

//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;
//import static personalCalendar.models.CalendarEvent.TIME_FORMAT;

public class Unbook implements Operation {
    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //region Constructors
    public Unbook(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser=fileParser;
        this.instructions=instructions;
    }
    //endregion

    //region Methods
    @Override
    public boolean execute() {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);

        try {
            CalendarEvent event=new CalendarEvent(null,date,startTime,endTime,null);
            if(fileParser.getFileContent().removeIf(event::equals))
            {
                System.out.printf("Event successfully unbooked:  %s %s %s",date,startTime,endTime);
                return true;
            }
            else
                System.out.printf("There is no such event booked: %s %s %s",date,startTime,endTime);

            return false;
        } catch (CalendarDateException e) {
            System.out.println("Please input correct data format: "+ DATE_FORMATTER);
            return false;
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format: "+ TIME_FORMATTER);
            return false;
        }catch (InvalidTimeIntervalException ex){
            System.out.println("Incorrect input! Please note that endTime must be after startTime.");
            return false;
        }
    }
    //endregion
}
