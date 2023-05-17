package project.models.operations.inqueries;

import project.contracts.CalendarOperation;
import project.exceptions.*;
import project.models.calendar.CalendarService;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.parsers.LocalDateParser;
import project.models.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

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
     * Current loaded calendar.
     */
    private PersonalCalendar loadedCalendar;

    /**
     * Date to search from.
     */
    private LocalDate date;

    /**
     * Set of calendars to be merged.
     */
    private Set<PersonalCalendar> personalCalendars;

    /**
     * Sublist of passed instructions.
     */
    private List<String> subListOfInstructions;

    /**
     * Filtered caledar events from loaded calendar
     */
    private HashSet<CalendarEvent> loadedCalendarEventsFiltered;

    /**
     * Constructs a FindSlotWith object with the provided CalendarService and instruction list.
     * @param calendarService The CalendarService object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public FindSlotWith(CalendarService calendarService, List<String> instructions) throws CalendarException {
        loadedCalendar=calendarService.getRepository();
        personalCalendars=new HashSet<>();
        date= LocalDateParser.parse(instructions.get(0));
        subListOfInstructions=instructions.subList(0,2);

        loadedCalendarEventsFiltered=new HashSet<>(loadedCalendar.getCalendarEvents().stream().filter(item -> item.getDate().equals(date)).toList());

        for(int i=2;i<instructions.size();i++) {
            String externalFileDirectory = instructions.get(i);

            if (!externalFileDirectory.endsWith(".xml"))
                externalFileDirectory += ".xml";

            File file = new File(externalFileDirectory);

            if (calendarService.getLoadedFile().equals(file)) {
                System.out.println("You can't pass as an argument currently opened calendar.\n");
                continue;
            }

            if (calendarService.getLoadedFile().exists()) {
                PersonalCalendar calendar = calendarService.getParser().readFile(new File(externalFileDirectory));
                calendar.setName(externalFileDirectory);
                personalCalendars.add(calendar);
            } else{
                throw new OperationException("File " + externalFileDirectory + "does not exist.\nOperation cancelled");
            }
        }
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
    public void execute() throws CalendarException {

        if(checkIfEventExistInCalendar(date,loadedCalendar.getCalendarEvents())) {

            FindSlot findSlot = new FindSlot(loadedCalendar, subListOfInstructions);

            if (findSlot.findFreeSpaceInCalendar().isEmpty()) {
                throw new OperationException("There is no free space in loaded calendar");
            }
        }

        for(PersonalCalendar externalPersonalCalendar:personalCalendars){

            if(checkIfEventExistInCalendar(date,loadedCalendar.getCalendarEvents())) {

                FindSlot findSlot = new FindSlot(externalPersonalCalendar, subListOfInstructions);

                if (findSlot.findFreeSpaceInCalendar().isEmpty())
                    continue;
            }

            HashSet<CalendarEvent> externalCalendarEventsFiltered=new HashSet<>(externalPersonalCalendar.getCalendarEvents().stream().filter(item -> item.getDate().equals(date)).toList());

            PersonalCalendar mixedPersonalCalendar =new PersonalCalendar();
            mixedPersonalCalendar.setCalendarEvents(combineCalendars(externalCalendarEventsFiltered));

            System.out.print("\n"+externalPersonalCalendar.getName()+" - ");
            new FindSlot(mixedPersonalCalendar,subListOfInstructions).execute();
        }
    }

    //region InternalMethods
    /**
     * Checks if there are any events in the specified calendar for the given date.
     * @param dateToSearch      the date to search for events
     * @param calendarEvents    the calendar events to search
     * @return true if there are any events for the given date, false otherwise
     */
    private boolean checkIfEventExistInCalendar(LocalDate dateToSearch,Set<CalendarEvent> calendarEvents){
        for (CalendarEvent event : calendarEvents) {
            if (event.getDate().equals(dateToSearch)) {
                if (!event.isHoliday()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Combines the events from two calendars into a single calendar,
     * removing any events that occur at the same time and are incompatible.
     * @param firstCalendar the first calendar to combine
     * @param secondCalendar the second calendar to combine
     * @return the combined calendar
     */
    private HashSet<CalendarEvent> combineCalendars(HashSet<CalendarEvent> secondCalendar) throws CalendarDateException, InvalidTimeIntervalException {
        HashSet<CalendarEvent> combinedEventsByDate=new HashSet<>();

        HashSet<CalendarEvent> bannedEvents=new HashSet<>();


        for(CalendarEvent firstCalendarEvent: loadedCalendarEventsFiltered){

            for(CalendarEvent secondCalendarEvent: secondCalendar){

                if(combinedEventsByDate.contains(secondCalendarEvent)||combinedEventsByDate.contains(firstCalendarEvent))
                    continue;

                if(firstCalendarEvent.equals(secondCalendarEvent)) {
                    combinedEventsByDate.add(firstCalendarEvent);
                    continue;
                }

                if(firstCalendarEvent.checkCompatibility(secondCalendarEvent))
                    continue;

                CalendarEvent newCalendarEvent=generateSpecialEvent(firstCalendarEvent,secondCalendarEvent);

                for(CalendarEvent event:combinedEventsByDate){
                    if(!newCalendarEvent.checkCompatibility(event)){
                        combinedEventsByDate.remove(event);
                        newCalendarEvent=generateSpecialEvent(newCalendarEvent,event);
                        break;
                    }
                }

                combinedEventsByDate.add(newCalendarEvent);
                bannedEvents.add(firstCalendarEvent);
                bannedEvents.add(secondCalendarEvent);
            }
        }

        combinedEventsByDate.addAll(loadedCalendarEventsFiltered);
        combinedEventsByDate.addAll(secondCalendar);

        for(CalendarEvent event:bannedEvents){
            combinedEventsByDate.remove(event);
        }

        return combinedEventsByDate;
    }


    private CalendarEvent generateSpecialEvent(CalendarEvent firstCalendarEvent,CalendarEvent secondCalendarEvent) throws CalendarDateException, InvalidTimeIntervalException {
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

        return new CalendarEvent("", date, minStartTime, maxEndTime, "");
    }

    //endregion
}

