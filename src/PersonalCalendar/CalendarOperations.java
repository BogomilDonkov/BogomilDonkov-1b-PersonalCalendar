package PersonalCalendar;

import CommandLineInterface.Operations;
import CommandLineInterface.Parsers.FileParser;
import PersonalCalendar.Exceptions.CalendarDateException;
import PersonalCalendar.Exceptions.CalendarTimeException;

import java.util.ArrayList;

import static PersonalCalendar.CalendarEvent.*;

public class CalendarOperations extends Operations<FileParser> {

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CalendarOperations(FileParser fileParser) {
        super(fileParser);
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void book(ArrayList<String> instructions){
        if(super.getFileParser().getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return;
        }

        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        String name=instructions.get(3);
        String note=instructions.get(4);

        try {
            getFileParser().getFileContent().add(new CalendarEvent(name,date,startTime,endTime,note));
        } catch (CalendarDateException e) {
            System.out.println("Please input correct data format "+dateFormat);
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format "+timeFormat);
        }
    }

    public void unbook(ArrayList<String> instructions){
        if(super.getFileParser().getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return;
        }

        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);

        try {
            CalendarEvent event=new CalendarEvent(null,date,startTime,endTime,null);
            getFileParser().getFileContent().removeIf(event::equals);

        } catch (CalendarDateException e) {
            System.out.println("Please input correct data format "+dateFormat);
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format "+timeFormat);
        }
    }





}
