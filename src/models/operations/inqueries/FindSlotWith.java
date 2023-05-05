package models.operations.inqueries;

import contracts.CalendarOperation;
import exceptions.CalendarDateException;
import exceptions.OperationException;
import models.calendar.PersonalCalendar;
import models.calendar.CalendarEvent;
import parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static models.calendar.CalendarEvent.DATE_FORMATTER;
import static models.calendar.CalendarEvent.DATE_PATTERN;

/**
 * A class that represents a find-slot-with operation in a calendar.
 * This operation finds a time slot with the requested duration
 * for an event in the calendar, given a date and a set of instructions.
 * It searches for free slots in the calendar and other external calendars,
 * then combines the calendars to find the first available slot
 * and returns the start and end times of that slot.
 */
public class FindSlotWith implements CalendarOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final XMLParser xmlParser;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs a FindSlotWith object with the provided XMLParser and instruction list.
     * @param xmlParser The XMLParser object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public FindSlotWith(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions = instructions;
    }

    /**
     * Executes the find-slot-with operation.
     * Finds a time slot with the requested duration for an event in the calendar,
     * given a date and a set of instructions.
     * Searches for free slots in the calendar and other external calendars,
     * then combines the calendars to find the first available slot
     * and returns the start and end times of that slot.
     * @throws OperationException  if an error occurs while executing the operation
     * @throws CalendarDateException if the input date is in invalid format.
     */
    @Override
    public void execute() throws OperationException, CalendarDateException {
        LocalDate date;
        try {
            date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
        }catch (DateTimeParseException e){
            throw new CalendarDateException("Invalid date format. Please use "+ DATE_PATTERN);
        }

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(xmlParser.getCalendar().getCalendarEvents());

        //Проверяваме дали имаме резервирано събития за дадената дата.
        //Ако няма такова, правим проверка дали е възможно да се запази поне едно събитие с дадената продължителност.
        //Ако не може, функцията връща false
        if(checkIfEventExistInCalendar(date,calendarEvents)) {
            ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));
            FindSlot findSlot = new FindSlot(xmlParser.getCalendar(), subListInstructions);
            if (findSlot.findFreeSpaceInCalendar().isEmpty()) {
                throw new OperationException("There is no free space in loaded calendar");
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
            HashSet<CalendarEvent> externalFileCalendarEvents;
            try {
                externalFileCalendarEvents = new HashSet<>(xmlParser.readFile(externalFileDirectory));
            }catch (JAXBException e)
            {
                throw new OperationException("Cannot open: "+externalFileDirectory);
            }
            //Проверяваме дали имаме резервирано събития за дадената дата.
            //Ако няма такова, правим проверка дали е възможно да се запази поне едно събитие с дадената продължителност.
            //Ако не може прескачаме този цикъл
            if(checkIfEventExistInCalendar(date,calendarEvents)) {
                PersonalCalendar externalPersonalCalendar = new PersonalCalendar();
                externalPersonalCalendar.setCalendarEvents(externalFileCalendarEvents);
                ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));
                FindSlot findSlot = new FindSlot(externalPersonalCalendar, subListInstructions);
                if (findSlot.findFreeSpaceInCalendar().isEmpty())
                    continue;
            }

            //Намираме и запазваме всички събития от календара, зареден в програмата, с подадената дата от потребителя в колекция
            HashSet<CalendarEvent> loadedCalendarEventsFiltered=new HashSet<>(calendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());

            //Намираме и запазваме всички събития от календара, подаден от потребителя, с подадената дата от потребителя в колекция
            HashSet<CalendarEvent> externalCalendarEventsFiltered=new HashSet<>(externalFileCalendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());

            ArrayList<String> subListInstructions = new ArrayList<>(instructions.subList(0, 2));

            PersonalCalendar mixedPersonalCalendar =new PersonalCalendar();
            mixedPersonalCalendar.setCalendarEvents(combineCalendars(loadedCalendarEventsFiltered,externalCalendarEventsFiltered));

            FindSlot findSlot=new FindSlot(mixedPersonalCalendar,subListInstructions);
            findSlot.execute();
        }
    }


    //region InternalMethods
    /**
     * Checks if there are any events in the specified calendar for the given date.
     * @param dateToSearch      the date to search for events
     * @param calendarEvents    the calendar events to search
     * @return true if there are any events for the given date, false otherwise
     */
    private boolean checkIfEventExistInCalendar(LocalDate dateToSearch,HashSet<CalendarEvent> calendarEvents){
        for (CalendarEvent event : calendarEvents)
            if (event.getDate().equals(dateToSearch))
                if (!event.isHoliday())
                    return true;

        return false;
    }

    /**
     * Combines the events from two calendars into a single calendar,
     * removing any events that occur at the same time and are incompatible.
     * @param firstCalendar the first calendar to combine
     * @param secondCalendar the second calendar to combine
     * @return the combined calendar
     */
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

