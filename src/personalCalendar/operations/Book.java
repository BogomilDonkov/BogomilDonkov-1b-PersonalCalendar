package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;
import personalCalendar.exceptions.CalendarDateException;
import personalCalendar.exceptions.CalendarTimeException;
import personalCalendar.exceptions.InvalidTimeIntervalException;

import java.util.ArrayList;

import static personalCalendar.models.CalendarEvent.*;

public class Book implements Operation {

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Book(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser=fileParser;
        this.instructions=instructions;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        String name=instructions.get(3);

        StringBuilder builder=new StringBuilder();
        for(String string:instructions.subList(4,instructions.size()))
        {
            builder.append(string);
            builder.append(" ");
        }

        String note= builder.toString();

        try {
            CalendarEvent calendarEvent=new CalendarEvent(name,date,startTime,endTime,note);
            boolean isCompatible=true;
            CalendarEvent incompatibleEvent = null;
            for(CalendarEvent event:fileParser.getFileContent())
            {
                if(!calendarEvent.checkCompatibility(event))
                {
                    isCompatible=false;
                    incompatibleEvent=event;
                }
            }

            if(isCompatible)
                if(fileParser.getFileContent().add(calendarEvent))
                    System.out.printf("Event successfully booked %s %s %s %s %s\n",name,date,startTime,endTime,note);
                else
                    System.out.printf("Event is already booked %s %s %s %s %s\n",name,date,startTime,endTime,note);
            else
                System.out.printf("The event you have typed is currently incompatible with event: %s\n",incompatibleEvent);

            return true;
        } catch (CalendarDateException e) {
            System.out.println("Please input correct date format "+ DATE_FORMATTER);
            return false;
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format "+ TIME_FORMATTER);
            return false;
        }catch (InvalidTimeIntervalException ex){
            System.out.println("Incorrect input! Please note that endTime must be after startTime.");
            return false;
        }
    }
}
