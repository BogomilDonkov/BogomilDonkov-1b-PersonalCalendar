package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

import static personalCalendar.models.CalendarEvent.DATE_FORMATTER;

//import static personalCalendar.models.CalendarEvent.DATE_FORMAT;

public class Busydays implements Operation {
    //Members~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~
    public Busydays(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //Methods~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
       // try {
            //Date startDate = DATE_FORMAT.parse(instructions.get(0));
            //Date endDate = DATE_FORMAT.parse(instructions.get(1));

            LocalDate startDate = LocalDate.parse(instructions.get(0), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(instructions.get(0), DATE_FORMATTER);

            ArrayList<CalendarEvent> events=new ArrayList<>();

            for(CalendarEvent event:fileParser.getFileContent()){
                if((event.getDate().isAfter(startDate)||event.getDate().equals(startDate))&&
                        (event.getDate().isAfter(endDate)||event.getDate().equals(endDate))){
                    events.add(event);
                }
            }

            Comparator<CalendarEvent> comparator= (o1, o2) -> {

                o2.getEndTime().until(o2.getStartTime(), ChronoUnit.HOURS);

                if(o1.getEndTime().until(o1.getStartTime(), ChronoUnit.HOURS)==o2.getEndTime().until(o2.getStartTime(), ChronoUnit.HOURS))
                {
                    if(o1.getDate().compareTo(o2.getDate())==0){
                        return (int)o1.getStartTime().until(o2.getStartTime(), ChronoUnit.HOURS);
                    }

                    return o1.getDate().compareTo(o2.getDate());
                }

                if(o1.getEndTime().until(o1.getStartTime(), ChronoUnit.HOURS) > o2.getEndTime().until(o2.getStartTime(), ChronoUnit.HOURS)){
                    return 1;
                }
                return -1;
            };

            events.sort(comparator);

            for(CalendarEvent event:events){
                System.out.println(event);
            }

            return true;

       // } catch (ParseException e) {
       //     System.out.println("Please input correct data format: "+ dateFormatter);
       // }
    }
}
