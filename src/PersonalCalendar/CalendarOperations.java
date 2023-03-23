package PersonalCalendar;

import CommandLineInterface.Operations;
import CommandLineInterface.Parsers.FileParser;
import PersonalCalendar.Exceptions.CalendarDateException;
import PersonalCalendar.Exceptions.CalendarTimeException;
import PersonalCalendar.Exceptions.InvalidTimeIntervalException;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import static PersonalCalendar.CalendarEvent.*;

public class CalendarOperations extends Operations<FileParser> {

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CalendarOperations(FileParser fileParser) {
        super(fileParser);
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String help() {
        return super.help()+ """             
                \tCalendar commands:\s
                \t\t\tbook <date> <startTime> <endTime> <name> <note>       Books an event using given arguments.\s
                \t\t\tunbook <date> <startTime> <endTime>                   Unbooks an event using given arguments.\s
                \t\t\tagenda <date>                                         Prints all events for given date in chronological order.\s
                \t\t\tchange <date> <startTime> <option> <newValue>         Select the event you want to update with <date> and <startTime>.\s
                \t\t\t                                                      <option> takes : date,startTime,endTime,name or note as argument.\s
                \t\t\t                                                      With <newValue> we update the value to the current <option>.\s
                \t\t\tfind <string>                                         Prints all events that contains the given string in their name or note.\s
                \t\t\tholiday                                               ---------\s
                \t\t\tbusydays                                              ---------\s
                \t\t\tfindslot                                              ---------\s
                \t\t\tfindslotwith                                          ---------\s
                \t\t\tmerge                                                 ---------\s
                """;
    }

    public void book(ArrayList<String> instructions){
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);
        String name=instructions.get(3);
        String note=instructions.get(4);

        try {
            CalendarEvent calendarEvent=new CalendarEvent(name,date,startTime,endTime,note);
            boolean isCompatible=true;
            CalendarEvent incompatibleEvent = null;
            for(CalendarEvent event:getFileParser().getFileContent())
            {
                if(!calendarEvent.checkCompatibility(event))
                {
                    isCompatible=false;
                    incompatibleEvent=event;
                }
            }

            if(isCompatible)
                if(getFileParser().getFileContent().add(calendarEvent))
                    System.out.printf("Event successfully booked %s %s %s %s %s",name,date,startTime,endTime,note);
                else
                    System.out.printf("Event is already booked %s %s %s %s %s",name,date,startTime,endTime,note);
            else
                System.out.printf("The event you have typed is currently incompatible with event: %s",incompatibleEvent);

        } catch (CalendarDateException e) {
            System.out.println("Please input correct date format "+DATE_FORMAT.toPattern());
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format "+TIME_FORMAT.toPattern());
        }catch (InvalidTimeIntervalException ex){
            System.out.println("Incorrect input! Please note that endTime must be after startTime.");
        }

    }

    public void unbook(ArrayList<String> instructions){
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String endTime=instructions.get(2);

        try {
            CalendarEvent event=new CalendarEvent(null,date,startTime,endTime,null);
            if(getFileParser().getFileContent().removeIf(event::equals))
                System.out.printf("Event successfully unbooked:  %s %s %s",date,startTime,endTime);
            else
                System.out.printf("There is no such event booked: %s %s %s",date,startTime,endTime);

        } catch (CalendarDateException e) {
            System.out.println("Please input correct data format: "+ DATE_FORMAT);
        } catch (CalendarTimeException e) {
            System.out.println("Please input correct time format: "+TIME_FORMAT);
        }catch (InvalidTimeIntervalException ex){
            System.out.println("Incorrect input! Please note that endTime must be after startTime.");
        }
    }

    public void agenda(ArrayList<String> instructions){
        try {
            Date date=DATE_FORMAT.parse(instructions.get(0));
            ArrayList<CalendarEvent> calendarEvents=new ArrayList<>(getFileParser().getFileContent());

            if(calendarEvents.isEmpty())
            {
                System.out.println("There are no events within the current set date: "+DATE_FORMAT.format(date));
                return;
            }

            Collections.sort(calendarEvents);
            System.out.println("Date\t\t\tStartTime\tEndTime\t\tName\t\t\tNote");

           for(CalendarEvent event:calendarEvents){
               if(event.getDate().equals(date)){
                   System.out.println(event);
               }
           }

        } catch (ParseException e) {
            System.out.println("Please input correct date format "+DATE_FORMAT.toPattern());;
        }
    }

    public void change(ArrayList<String> instructions){
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String option=instructions.get(2);
        String newValue=instructions.get(3);

        try {
            CalendarEvent calendarEvent=new CalendarEvent(date,startTime);
            for(CalendarEvent event:getFileParser().getFileContent())
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
                            try {
                                calendarEvent.setDate(DATE_FORMAT.parse(newValue));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            } catch (ParseException e) {
                                System.out.println("Please input correct data format: "+ DATE_FORMAT);
                            }
                        }

                        case "startTime" ->{
                            try {
                                calendarEvent.setStartTime(TIME_FORMAT.parse(newValue));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            } catch (ParseException e) {
                                System.out.println("Please input correct time format: "+TIME_FORMAT);
                            }
                        }

                        case "endTime" ->{
                            try {
                                calendarEvent.setEndTime(TIME_FORMAT.parse(newValue));
                                checkAndUpdateCalendarEventSet(calendarEvent,event);
                            } catch (ParseException e) {
                                System.out.println("Please input correct time format: "+TIME_FORMAT);
                            }
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
                    return;
                }
            }
            System.out.println("There is no such event in the calendar.");

            } catch (CalendarDateException ex) {
            System.out.println("Please input correct data format: "+ DATE_FORMAT);
        } catch (CalendarTimeException ex) {
            System.out.println("Please input correct time format: "+TIME_FORMAT);
        }
    }

    public void find(ArrayList<String> instructions){
        HashSet<CalendarEvent> foundedEvents=new HashSet<>();

        StringBuilder string=new StringBuilder();
        for(String value:instructions)
            string.append(value);

        for(CalendarEvent event:getFileParser().getFileContent()){
            if(event.getName().contains(string)||event.getNote().contains(string)){
                foundedEvents.add(event);
            }
        }

        if(!foundedEvents.isEmpty()){
            System.out.println("Here are the events that contain "+string);
            for(CalendarEvent event:foundedEvents){
                System.out.println(event);
            }
        }
        else
            System.out.println("There are no events that contain: "+string);
    }

    public void holiday(ArrayList<String> instructions){
        try {
            Date date= DATE_FORMAT.parse(instructions.get(0));

            for(CalendarEvent event:getFileParser().getFileContent()){
                if(event.getDate().equals(date)){
                    event.setHoliday(true);
                }
            }

        } catch (ParseException e) {
            System.out.println("Please input correct data format: "+ DATE_FORMAT);
        }
    }

    public void busydays(ArrayList<String> instructions) {
        try {
            Date startDate = DATE_FORMAT.parse(instructions.get(0));
            Date endDate = DATE_FORMAT.parse(instructions.get(1));
            ArrayList<CalendarEvent> events=new ArrayList<>();


            for(CalendarEvent event:getFileParser().getFileContent()){
                if((event.getDate().after(startDate)||event.getDate().equals(startDate))&&
                        (event.getDate().before(endDate)||event.getDate().equals(endDate))){
                    events.add(event);
                }
            }

            Comparator<CalendarEvent> comparator= (o1, o2) -> {
                if(o1.getEndTime().getTime()-o1.getStartTime().getTime()==o2.getEndTime().getTime()-o2.getStartTime().getTime())
                {
                    if(o1.getDate().compareTo(o2.getDate())==0){
                        return (int)(o1.getStartTime().getTime()-o2.getStartTime().getTime());
                    }

                    return o1.getDate().compareTo(o2.getDate());
                }

                if(o1.getEndTime().getTime()-o1.getStartTime().getTime()>o2.getEndTime().getTime()-o2.getStartTime().getTime()){
                    return 1;
                }
                return -1;
            };

            events.sort(comparator);

            for(CalendarEvent event:events){
                System.out.println(event);
            }


        } catch (ParseException e) {
            System.out.println("Please input correct data format: "+ DATE_FORMAT);
        }
    }

    public void findslot(ArrayList<String> instructions){
        try {
            Date date = DATE_FORMAT.parse(instructions.get(0));
            int hours= Integer.parseInt(instructions.get(1));
            ArrayList<CalendarEvent> events=new ArrayList<>();

            for(CalendarEvent event:getFileParser().getFileContent()){
                if(event.getDate().equals(date)){
                    long diff=(event.getEndTime().getTime()-event.getStartTime().getTime());
                    diff/=3600000;
                    if(diff==hours){
                        events.add(event);
                    }
                }
            }

            for(CalendarEvent event:events){
                System.out.println(event);
            }

        } catch (ParseException e) {
            System.out.println("Please input correct data format: "+ DATE_FORMAT);
        }

    }

    private void checkAndUpdateCalendarEventSet(CalendarEvent newEvent,CalendarEvent oldEvent){
        boolean isCompatible=true;
        HashSet<CalendarEvent> incompatibleEvents = new HashSet<>();
        for(CalendarEvent event:getFileParser().getFileContent())
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
            getFileParser().getFileContent().remove(oldEvent);
            getFileParser().getFileContent().add(newEvent);
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
