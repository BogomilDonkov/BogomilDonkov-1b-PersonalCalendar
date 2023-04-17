package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;
import personalCalendar.exceptions.CalendarDateException;
import personalCalendar.exceptions.CalendarTimeException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;
import static personalCalendar.models.CalendarEvent.TIME_FORMATTER;

//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;
//import static personalCalendar.models.CalendarEvent.TIME_FORMAT;

public class Change implements Operation {
    //Members~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~
    public Change(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String option=instructions.get(2);
        String newValue=instructions.get(3);

        try {
            CalendarEvent calendarEvent=new CalendarEvent(date,startTime);
            for(CalendarEvent event:fileParser.getFileContent())
            {
                if(event.equals(calendarEvent)){
                    calendarEvent.setDate(event.getDate());
                    calendarEvent.setStartTime(event.getStartTime());
                    calendarEvent.setEndTime(event.getEndTime());
                    calendarEvent.setName(event.getName());
                    calendarEvent.setNote(event.getNote());
                    switch (option)
                    {
                        case "date"->{
                            //try {
                                //calendarEvent.setDate(DATE_FORMAT.parse(newValue));
                                calendarEvent.setDate(LocalDate.parse(newValue, DATE_FORMATTER));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            //} catch (ParseException e) {
                            //    System.out.println("Please input correct data format: "+ dateFormatter);
                            //}
                        }

                        case "startTime" ->{
                            //try {
                                //calendarEvent.setStartTime(TIME_FORMAT.parse(newValue));
                                calendarEvent.setStartTime(LocalTime.parse(newValue, TIME_FORMATTER));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            //} catch (ParseException e) {
                            //    System.out.println("Please input correct time format: "+TIME_FORMAT);
                            //}
                        }

                        case "endTime" ->{
                            //try {
                                //calendarEvent.setEndTime(TIME_FORMAT.parse(newValue));
                                calendarEvent.setEndTime(LocalTime.parse(newValue, TIME_FORMATTER));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            //} catch (ParseException e) {
                            //    System.out.println("Please input correct time format: "+TIME_FORMAT);
                            //}
                        }

                        case "name" ->{
                            event.setName(newValue);
                        }

                        case "note" ->{
                            for(int i=4;i<instructions.size();i++)
                            {
                                newValue+=" "+instructions.get(i);
                            }
                            event.setNote(newValue);
                        }

                        default -> System.out.println(option+" is not recognized as internal command.");
                    }
                    return true;
                }
            }
            System.out.println("There is no such event in the calendar.");
            return false;

        } catch (CalendarDateException ex) {
            System.out.println("Please input correct data format: "+ DATE_FORMATTER);
            return false;
        } catch (CalendarTimeException ex) {
            System.out.println("Please input correct time format: "+ TIME_FORMATTER);
            return false;
        }
    }

    private void checkAndUpdateCalendarEventSet(CalendarEvent newEvent,CalendarEvent oldEvent){
        boolean isCompatible=true;
        HashSet<CalendarEvent> incompatibleEvents = new HashSet<>();
        for(CalendarEvent event:fileParser.getFileContent())
        {
            if(event.equals(oldEvent))
                continue;

            if(!newEvent.checkCompatibility(event))
            {
                isCompatible=false;
                incompatibleEvents.add(event);
            }
        }

        if(isCompatible) {
            fileParser.getFileContent().remove(oldEvent);
            fileParser.getFileContent().add(newEvent);
        }
        else {
            System.out.println("The event you have typed is currently incompatible with event\\s:");
            System.out.println("Date\t\t\tStartTime\tEndTime\t\tName\t\t\tNote");
            for(CalendarEvent event:incompatibleEvents){
                System.out.println(event);
            }
        }
    }
}
