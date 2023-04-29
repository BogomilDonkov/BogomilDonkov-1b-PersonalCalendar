package operations.calendarOp;

import contracts.Operation;
import models.Calendar;
import models.CalendarEvent;

import java.util.ArrayList;
import java.util.HashSet;

public class Find implements Operation<Boolean> {

    private final Calendar calendar;
    private final ArrayList<String> instructions;


    public Find(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }



    @Override
    public Boolean execute() {
        HashSet<CalendarEvent> foundedEvents=new HashSet<>();
        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        StringBuilder stringToSearch=new StringBuilder();
        int counter=0;
        for(String value:instructions) {
            stringToSearch.append(value);
            if(counter++!=instructions.size()-1)
                stringToSearch.append(" ");
        }

        for(CalendarEvent event:calendarEvents){
            String eventNameCaseInsensitive=event.getName().toLowerCase();
            String eventNoteCaseInsensitive=event.getNote().toLowerCase();
            String stringToSearchCaseInsensitive=stringToSearch.toString().toLowerCase();

            if(eventNameCaseInsensitive.contains(stringToSearchCaseInsensitive)||eventNoteCaseInsensitive.contains(stringToSearchCaseInsensitive)){
                foundedEvents.add(event);
            }
        }

        if(!foundedEvents.isEmpty()){
            System.out.println("Here are the events that contain "+stringToSearch);
            for(CalendarEvent event:foundedEvents){
                System.out.println(event);
            }
            return true;
        }
        else
            System.out.println("There are no events that contain: "+stringToSearch);

        return false;
    }

}
