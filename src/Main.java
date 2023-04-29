import exceptions.CalendarDateException;
import exceptions.CalendarTimeException;
import exceptions.InvalidTimeIntervalException;

import models.*;
import operations.calendarOp.*;
import operations.defaultOp.*;
import parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        //Calendar calendar=new Calendar();
////
        //try{
        //    calendar.addEvent(new CalendarEvent("Swimming","23-04-2023","09:30","10:00","Don't be late!"));
        //    calendar.addEvent(new CalendarEvent("Swimming","23-04-2023","10:30","11:00","Don't be late!"));
        //    calendar.addEvent(new CalendarEvent("Swimming","25-04-2023","15:30","19:00","Don't be late!"));
        //    calendar.addEvent(new CalendarEvent("Swimming","26-04-2023","16:30","19:00","Don't be late!"));
        //}
        //catch (CalendarDateException | InvalidTimeIntervalException | CalendarTimeException e) {
        //    System.out.printf(e.getMessage());
        //}

        XMLParser xmlParser=new XMLParser();

        ArrayList<String> arrayList=new ArrayList<>();

        arrayList.add("Calendar.xml");

        Open open=new Open(xmlParser,arrayList);
        open.execute();

        arrayList=new ArrayList<>();
        arrayList.add("SportCalendar.xml");
        //arrayList.add("AnotherSportCalendar.xml");

        //FindSlotWith findSlotWith=new FindSlotWith(xmlParser,arrayList);
        //findSlotWith.execute();

        //Merge merge=new Merge(xmlParser,arrayList);
        //merge.execute();
//
        //Save save=new Save(xmlParser);
        //save.execute();

        Help help=new Help();
        System.out.println(help.execute());

    }
}