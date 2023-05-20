package project.models.operations.manipulations;

import project.contracts.CalendarOperation;
import project.exceptions.CalendarDateException;
import project.exceptions.CalendarException;
import project.exceptions.CalendarTimeException;
import project.exceptions.OperationException;
import project.models.calendar.CalendarService;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;
import project.models.parsers.LocalDateParser;
import project.models.parsers.LocalTimeParser;
import project.models.parsers.XMLParser;
import project.util.CalendarScanner;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * The Merge class implements the CalendarOperation interface and is responsible for merging multiple calendars into a single one.
 */
public class Merge implements CalendarOperation {

    /**
     * The repository of the project
     */
    private PersonalCalendar loadedCalendar;
    /**
     * Currently opened file.
     */
    private File openedFile;
    /**
     * Map of all passed calendar names and their {@link CalendarEvent}'s
     */
    private Map<String,Set<CalendarEvent>> passedCalendars;

    /**
     * Constructs a Merge object with the provided CalendarService and instruction list.
     * @param calendarService The CalendarService object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Merge(CalendarService calendarService, List<String> instructions) throws OperationException {
        loadedCalendar=calendarService.getRepository();
        openedFile=calendarService.getLoadedFile();
        this.passedCalendars=new HashMap<>();

        for(String fileName: instructions){
            if(!fileName.endsWith(".xml"))
                fileName+=".xml";

            if(calendarService.getLoadedFile().exists())
                passedCalendars.put(fileName,new HashSet<>(calendarService.getParser().readFile(new File(fileName)).getCalendarEvents()));
            else
                throw new OperationException("File "+fileName + "does not exist.\nMerging was canceled");
        }
    }

    /**
     * Merges multiple XML calendar files into one.
     * The execute method reads in the names for the files to be merged, and performs
     * the merging operation. If any collisions are detected between events in the loaded
     * events and the events in the new events being read, the user will be prompted to
     * resolve the conflicts. The merged calendars will be added to the current calendar and saved as a new file.
     * @throws OperationException If the merging operation encounters an error or is interrupted by the user.
     */
    @Override
    public void execute() throws OperationException, CalendarDateException, CalendarTimeException {
        Set<CalendarEvent> loadedEvents= loadedCalendar.getCalendarEvents();

        for(Map.Entry<String,Set<CalendarEvent>> entry:passedCalendars.entrySet()){
            String fileName=entry.getKey();
            Set<CalendarEvent> newCalendarEvents=entry.getValue();

            Map<CalendarEvent, HashSet<CalendarEvent>> collisionMap = new HashMap<>(getCollidedEvents(loadedEvents, newCalendarEvents));

            if(collisionMap.isEmpty()){
                loadedCalendar.addAll(newCalendarEvents);

                continue;
            }
            else{
                if(!askUserForCorrectionAndSubmitAnswer(collisionMap,fileName))
                    throw new OperationException("Merging between " + fileName + " and " + openedFile.getName() + " was stopped.");

                if (!resolveCollisions(loadedEvents, newCalendarEvents))
                    throw new OperationException("Merging between " + fileName + " and " + openedFile.getName() + " was stopped.");
            }
        }


        System.out.println("All calendars: were successfully merged to " + openedFile.getName() + ".");
    }


    //region Internal Methods
    /**
     * This method prompts the user to resolve any collisions between events from
     * the loaded events and the new events being read in from the given file.
     * @param collisionMap A map of collided events between the loaded events and new events.
     * @param fileName The name of the file being read.
     * @return True if the user accepts the proposed solution, false otherwise.
     * @throws OperationException If the merging operation encounters an error or is interrupted by the user.
     */
    private boolean askUserForCorrectionAndSubmitAnswer(Map<CalendarEvent,HashSet<CalendarEvent>> collisionMap,String fileName) throws OperationException {
        System.out.println("There is collision between events: ");
        for (Map.Entry<CalendarEvent, HashSet<CalendarEvent>> entry : collisionMap.entrySet()){
            System.out.println(entry.getKey()+ " from " + openedFile.getName() + " collided with:");
            entry.getValue().stream().forEach(System.out::print);
            System.out.println("from : "+ fileName+ "\n");
        }

        System.out.println("""
                                                                
                        If you want to proceed, you need to edit your events.
                        Do you want to proceed ?  (Press 'Y' to accept. Press any other key to cancel.)\s""");
        System.out.print(">");

        String option = CalendarScanner.scanNextLine();

        return option.equals("Y") || option.equals("y");
    }

    /**
     * Recursively resolves {@link CalendarEvent} collisions.
     * @param loadedEvents The set of loaded calendar events.
     * @param newCalendarEvents The set of new calendar events being read in.
     * @return True if all calendar events have been merged successfully, false otherwise.
     * @throws OperationException from {@link Merge#createNewEvent(CalendarEvent)} method.
     */
    private boolean resolveCollisions(Set<CalendarEvent> loadedEvents,Set<CalendarEvent> newCalendarEvents) throws OperationException, CalendarDateException, CalendarTimeException {

        CalendarEvent collidedEvent=getCollidedEvent(loadedEvents,newCalendarEvents);

        if(collidedEvent==null)
            return true;

        CalendarEvent newEvent=createNewEvent(collidedEvent);

        loadedCalendar.addEvent(newEvent);
        newCalendarEvents.remove(collidedEvent);

        return resolveCollisions(loadedCalendar.getCalendarEvents(),newCalendarEvents);
    }

    /**
     * A helper method that checks for collided events between two sets of events.
     * @param firstCalendarEvents the events from the current calendar
     * @param secondCalendarEvents the events from the given calendar
     * @return a map of the collided events
     */
    private Map<CalendarEvent, HashSet<CalendarEvent>> getCollidedEvents(Set<CalendarEvent> firstCalendarEvents, Set<CalendarEvent> secondCalendarEvents){
        Map<CalendarEvent, HashSet<CalendarEvent>> collisionMap = new HashMap<>();

        for (CalendarEvent firstCalendarEvent : firstCalendarEvents) {

            HashSet<CalendarEvent> collisionSet=new HashSet<>();

            for (CalendarEvent secondCalendarEvent : secondCalendarEvents)
                if (!secondCalendarEvent.checkCompatibility(firstCalendarEvent)) {
                    collisionSet.add(secondCalendarEvent);
                }

            if(!collisionSet.isEmpty())
                collisionMap.put(firstCalendarEvent,collisionSet);

        }

        return collisionMap;
    }

    /**
     * A helper method that checks for collided events between two sets of events.
     * @param firstCalendarEvents the events from the current calendar
     * @param secondCalendarEvents the events from the given calendar
     * @return a colided event from the Second Calendar Events, otherwise returns null
     */
    private CalendarEvent getCollidedEvent(Set<CalendarEvent> firstCalendarEvents, Set<CalendarEvent> secondCalendarEvents){

        for (CalendarEvent firstCalendarEvent : firstCalendarEvents)
            for (CalendarEvent secondCalendarEvent : secondCalendarEvents)
                if (!secondCalendarEvent.checkCompatibility(firstCalendarEvent))
                    return secondCalendarEvent;

        return null;
    }



    /**
     * Creates a new calendar event based on the given old event, with user input for the date, start time, and end time.
     * @param oldEvent the old calendar event to base the new event on
     * @return the new calendar event
     * @throws OperationException if the new calendar event is incompatible with the existing events in the calendar
     */
    private CalendarEvent createNewEvent(CalendarEvent collidedEvent) {
        System.out.println("\nEvent to change: ");
        System.out.println(collidedEvent);
        System.out.println("New values: <date> <startTime> <endTime> " + collidedEvent.getName() + " " + collidedEvent.getNote());

        while (true) {
            System.out.print(">");
            String newInput = CalendarScanner.scanNextLine();

            if (newInput.equals(""))
                continue;

            String regex="\\s+";
            ArrayList<String> newInstructions = new ArrayList<>(List.of(newInput.split(regex)));

            if (newInstructions.isEmpty())
                continue;

            if (newInstructions.size() != 3) {
                System.out.println("expects <date> <startTime> <endTime>");
                continue;
            }


            LocalDate date;
            LocalTime startTime;
            LocalTime endTime;

            try {
                date = LocalDateParser.parse(newInstructions.get(0));
                startTime= LocalTimeParser.parse(newInstructions.get(1));
                endTime= LocalTimeParser.parse(newInstructions.get(2));
            } catch (CalendarDateException | CalendarTimeException e) {
                System.out.println(e.getMessage());
                continue;
            }

            CalendarEvent newCalendarEvent;

            try {
                newCalendarEvent = new CalendarEvent(collidedEvent.getName(),date, startTime, endTime,  collidedEvent.getNote());
            } catch (CalendarException e) {
                System.out.println(e.getMessage());
                continue;
            }

            Set<CalendarEvent> incompatibleEvents=loadedCalendar.checkIfAllEventAreCompatibleWithCalendar(newCalendarEvent);

            if(incompatibleEvents.isEmpty())
                return newCalendarEvent;


            StringBuilder descriptionBuilder=new StringBuilder();
            for (CalendarEvent incompatibleEvent : incompatibleEvents) {
                descriptionBuilder.append(incompatibleEvent);
                descriptionBuilder.append("\n");
            }

            System.out.println(newCalendarEvent + "\n is currently incompatible with:"+descriptionBuilder);
            System.out.println("Please type again ");
        }
    }
    //endregion
}
