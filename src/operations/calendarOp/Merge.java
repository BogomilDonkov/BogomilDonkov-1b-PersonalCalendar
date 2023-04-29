package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarException;
import models.Calendar;
import models.CalendarEvent;
import parsers.XMLParser;

import java.util.*;

public class Merge implements Operation<Boolean> {

    private final XMLParser xmlParser;
    private final ArrayList<String> instructions;


    public Merge(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute() {
        Calendar calendar = xmlParser.getCalendar();

        for(String fileName:instructions){

            //Записваме в променлива събитията от подадения календар
            Set<CalendarEvent> newCalendarEvents= new HashSet<>(xmlParser.readFile(fileName));;

            //Ключовете са събитията на текущия календар, които се сблъскват със събитията от подадения календар
            Map<CalendarEvent, CalendarEvent> collisionMap = new HashMap<>(checkCollidedEvents(calendar.getCalendarEvents(),newCalendarEvents));

            //Проверяваме дали е настъпила колизия между събития
            if (!collisionMap.isEmpty()) {
                System.out.println("There is collision between events: ");

                //Извеждаме между кои събития имат колизия
                for (Map.Entry<CalendarEvent, CalendarEvent> entry : collisionMap.entrySet())
                    System.out.println(entry.getKey() + " from " + xmlParser.getFile().getName() + " collided with:\n" + entry.getValue() + " from " + fileName);

                //Правим запитване до потребителя дали иска да редактира датата и часа на несъвместимите събития
                System.out.println("""
                                               
                        If you want to proceed, you need to edit your events.
                        Do you want to proceed ?  (Press 'Y' to accept. Press any other key to cancel.)\s""");
                System.out.print(">");

                //Прочитаме отговора на потребителя в променлива и извършваме обработка
                String option = new Scanner(System.in).nextLine();

               if(option.equals("Y") || option.equals("y")) {
                   //Минаваме през всяко едно несъвместимо събитие и приемаме новите стойности въведени от потребителя.
                   //Чрез новите стойности създаваме ново събитие и го добавяме в заредения календар
                   for (CalendarEvent event : collisionMap.values()) {
                       System.out.println("\nEvent to change: ");
                       System.out.println(event);
                       System.out.println("New values: <date> <startTime> <endTime> " + event.getName() + " " + event.getNote());

                       calendar.addEvent(createNewEvent(event));
                   }
                   calendar.addMergedCalendar(fileName);
                   continue;
               }
               else {
                   System.out.println(fileName + " was not merged into " + xmlParser.getFile().getName());
                   continue;
               }
            }

            calendar.addAll(newCalendarEvents);
            calendar.addMergedCalendar(fileName);
        }

        if(!xmlParser.getCalendar().getMergedCalendars().isEmpty())
            System.out.println("All calendars: were successfully merged to " + xmlParser.getFile().getName() + "!");
        return true;
    }


    //region Internal Methods

    private Map<CalendarEvent, CalendarEvent> checkCollidedEvents(Set<CalendarEvent> firstCalendarEvents, Set<CalendarEvent> secondCalendarEvents){

        //Ключовете са събитията на текущия календар, които се сблъскват със събитията от подадения календар
        Map<CalendarEvent, CalendarEvent> collidedEvents = new HashMap<>();

        //Проверяваме при кои събития от двата календара има колизия
        for (CalendarEvent event : firstCalendarEvents)
            for (CalendarEvent event1 : secondCalendarEvents)
                if (!event1.checkCompatibility(event))
                    collidedEvents.put(event, event1);
        return collidedEvents;
    }

    private CalendarEvent createNewEvent(CalendarEvent oldEvent){
        while (true) {
            System.out.print(">");
            String newInput = new Scanner(System.in).nextLine();
            if (newInput.equals(""))
                continue;
            ArrayList<String> newInstructions = new ArrayList<>(List.of(newInput.split("\\s+")));
            if (newInstructions.isEmpty())
                continue;
            if (newInstructions.size() != 3) {
                System.out.println("expects <date> <startTime> <endTime>");
                continue;
            }

            String date = newInstructions.get(0);
            String startTime = newInstructions.get(1);
            String endTime = newInstructions.get(2);
            CalendarEvent newCalendarEvent;

            try {
                newCalendarEvent = new CalendarEvent(oldEvent.getName(),date, startTime, endTime,  oldEvent.getNote());
            } catch (CalendarException e) {
                System.out.println(e.getMessage());
                continue;
            }

            Set<CalendarEvent> incompatibleEvents=xmlParser.getCalendar().checkIfAllEventAreCompatibleWithCalendar(newCalendarEvent);

            if(incompatibleEvents.isEmpty())
                return newCalendarEvent;

            System.out.println(newCalendarEvent + "\n is currently incompatible with:");

            for (CalendarEvent incompatibleEvent : incompatibleEvents) {
                System.out.println(incompatibleEvent + "\n");
            }
        }
    }

    //endregion
}
