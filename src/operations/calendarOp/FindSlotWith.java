package operations.calendarOp;

import contracts.Operation;
import exceptions.CalendarDateException;
import models.Calendar;
import models.CalendarEvent;
import parsers.XMLParser;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.CalendarEvent.DATE_FORMATTER;

public class FindSlotWith implements Operation<Boolean> {

    private final XMLParser xmlParser;
    private final ArrayList<String> instructions;


    public FindSlotWith(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions = instructions;
    }


    @Override
    public Boolean execute() {
        LocalDate date;
        try {
            date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (DateTimeParseException e){
            System.out.println(new CalendarDateException().getMessage());
            return null;
        }

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(xmlParser.getCalendar().getCalendarEvents());

        //Проверяваме дали имаме резервирано събития за дадената дата.
        //Ако няма такова, правим проверка дали е възможно да се запази поне едно събитие с дадената продължителност.
        //Ако не може, функцията връща false
        if(checkIfEventExistInCalendar(date,calendarEvents)) {
            ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));
            FindSlot findSlot = new FindSlot(xmlParser.getCalendar(), subListInstructions);
            if (findSlot.findFreeSpaceInCalendar().isEmpty()) {
                System.out.println("There is no free space in loaded calendar");
                return true;
            }
        }

        for(int i=2;i<instructions.size();i++) {
            String externalFileDirectory=instructions.get(i);//името на подадения от потребителя файл

            //Проверка дали е подаден същия календар като аргумент на командата
            File file=new File(instructions.get(i));

            if(xmlParser.getFile().equals(file)){
                System.out.println("You can't pass as an argument currently opened calendar.\n");
                continue;
            }

            //Запазваме в колекция всички събития от подадения календар
            HashSet<CalendarEvent> externalFileCalendarEvents=new HashSet<>(xmlParser.readFile(externalFileDirectory));

            //Проверяваме дали имаме резервирано събития за дадената дата.
            //Ако няма такова, правим проверка дали е възможно да се запази поне едно събитие с дадената продължителност.
            //Ако не може прескачаме този цикъл
            if(checkIfEventExistInCalendar(date,calendarEvents)) {
                Calendar externalCalendar = new Calendar();
                externalCalendar.setCalendarEvents(externalFileCalendarEvents);
                ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));
                FindSlot findSlot = new FindSlot(externalCalendar, subListInstructions);
                if (findSlot.findFreeSpaceInCalendar().isEmpty())
                    continue;
            }

            //Намираме и запазваме всички събития от календара, зареден в програмата, с подадената дата от потребителя в колекция
            HashSet<CalendarEvent> loadedCalendarEventsFiltered=new HashSet<>(calendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());

            //Намираме и запазваме всички събития от календара, подаден от потребителя, с подадената дата от потребителя в колекция
            HashSet<CalendarEvent> externalCalendarEventsFiltered=new HashSet<>(externalFileCalendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());

            ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));

            Calendar mixedCalendar=new Calendar();
            mixedCalendar.setCalendarEvents(combineCalendars(loadedCalendarEventsFiltered,externalCalendarEventsFiltered));

            FindSlot findSlot=new FindSlot(mixedCalendar,subListInstructions);
            findSlot.execute();
        }
        return true;
    }


    //region InternalMethods
    private boolean checkIfEventExistInCalendar(LocalDate dateToSearch,HashSet<CalendarEvent> calendarEvents){
        for (CalendarEvent event : calendarEvents)
            if (event.getDate().equals(dateToSearch))
                if (!event.isHoliday())
                    return true;

        return false;
    }
    private HashSet<CalendarEvent> combineCalendars(HashSet<CalendarEvent> firstCalendar,HashSet<CalendarEvent> secondCalendar) {
        HashSet<CalendarEvent> combinedEventsByDate=new HashSet<>();

        for(CalendarEvent firstCalendarEvent: firstCalendar){
            for(CalendarEvent secondCalendarEvent: secondCalendar)
            {
                if(firstCalendarEvent.equals(secondCalendarEvent)) {
                    combinedEventsByDate.add(firstCalendarEvent);
                    firstCalendar.remove(firstCalendarEvent);
                    secondCalendar.remove(secondCalendarEvent);
                }

                if(!firstCalendarEvent.checkCompatibility(secondCalendarEvent))
                {
                    LocalTime minStartTime;
                    LocalTime maxEndTime;

                    if(firstCalendarEvent.getStartTime().isAfter(secondCalendarEvent.getStartTime()))
                        minStartTime=secondCalendarEvent.getStartTime();
                    else
                        minStartTime=firstCalendarEvent.getStartTime();

                    if(firstCalendarEvent.getEndTime().isAfter(secondCalendarEvent.getEndTime()))
                        maxEndTime=firstCalendarEvent.getEndTime();
                    else
                        maxEndTime=secondCalendarEvent.getEndTime();

                    LocalDate date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);

                    try {
                        combinedEventsByDate.add(new CalendarEvent("", date, minStartTime, maxEndTime, ""));
                    }catch (Exception ignored){}

                    firstCalendar.remove(firstCalendarEvent);
                    secondCalendar.remove(secondCalendarEvent);
                }
            }
        }
        combinedEventsByDate.addAll(firstCalendar);
        combinedEventsByDate.addAll(secondCalendar);

        return combinedEventsByDate;
    }

    //endregion
}

