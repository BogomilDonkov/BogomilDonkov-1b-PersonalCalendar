package models.operations.manipulations;

import contracts.CalendarOperation;
import exceptions.CalendarException;
import exceptions.OperationException;
import models.calendar.PersonalCalendar;
import models.calendar.CalendarEvent;
import parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.util.*;

import static models.cli.CalendarCLI.scanner;

/**
 * The Merge class implements the CalendarOperation interface and is responsible for merging multiple calendars into a single one.
 */
public class Merge implements CalendarOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final XMLParser xmlParser;
    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs a Merge object with the provided XMLParser and instruction list.
     * @param xmlParser The XMLParser object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Merge(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions = instructions;
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
    public void execute() throws OperationException {
        PersonalCalendar currentPersonalCalendar =xmlParser.getCalendar();
        Set<CalendarEvent> loadedEvents= currentPersonalCalendar.getCalendarEvents();

        for(String fileName:instructions)
        {
            if(!fileName.endsWith(".xml"))
                fileName+=".xml";

            try {
                Set<CalendarEvent> newCalendarEvents = new HashSet<>(xmlParser.readFile(fileName));

                Map<CalendarEvent, HashSet<CalendarEvent>> collisionMap = new HashMap<>(getCollidedEvents(loadedEvents, newCalendarEvents));

                if(collisionMap.isEmpty()){
                    currentPersonalCalendar.addAll(newCalendarEvents);
                    currentPersonalCalendar.addMergedCalendar(fileName);
                    continue;
                }
                else{
                    if(!askUserForCorrectionAndSubmitAnswer(collisionMap,fileName))
                        throw new OperationException("Merging between " + fileName + " and " + xmlParser.getFile().getName() + " was stopped.");

                    if (resolveCollisions(loadedEvents, newCalendarEvents))
                        xmlParser.getCalendar().addMergedCalendar(fileName);
                    else
                        throw new OperationException("Merging between " + fileName + " and " + xmlParser.getFile().getName() + " was stopped.");
                }

                if (!xmlParser.getCalendar().getMergedCalendars().isEmpty())
                    System.out.println("All calendars: were successfully merged to " + xmlParser.getFile().getName() + ".");

            }catch (JAXBException e)
            {
                throw new OperationException("Cannot open file: "+fileName);
            }
        }
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
        System.out.println("There is collision between events.");
        for (Map.Entry<CalendarEvent, HashSet<CalendarEvent>> entry : collisionMap.entrySet()){
            System.out.println(entry.getKey()+ " from " + xmlParser.getFile().getName() + " collided with:");
            entry.getValue().stream().forEach(System.out::println);
            System.out.println(" ~~ From : "+ fileName+ "\n");
        }

        System.out.println("""
                                                                
                        If you want to proceed, you need to edit your events.
                        Do you want to proceed ?  (Press 'Y' to accept. Press any other key to cancel.)\s""");
        System.out.print(">");

        String option = scanner.nextLine();

        return option.equals("Y") || option.equals("y");
    }

    /**
     * Recursively resolves {@link CalendarEvent} collisions.
     * @param loadedEvents The set of loaded calendar events.
     * @param newCalendarEvents The set of new calendar events being read in.
     * @return True if all calendar events have been merged successfully, false otherwise.
     * @throws OperationException from {@link Merge#createNewEvent(CalendarEvent)} method.
     */
    private boolean resolveCollisions(Set<CalendarEvent> loadedEvents,Set<CalendarEvent> newCalendarEvents) throws OperationException {

        CalendarEvent collidedEvent=getCollidedEvent(loadedEvents,newCalendarEvents);

        if(collidedEvent==null)
            return true;

        CalendarEvent newEvent=createNewEvent(collidedEvent);
        xmlParser.getCalendar().addEvent(newEvent);
        newCalendarEvents.remove(collidedEvent);

        return resolveCollisions(xmlParser.getCalendar().getCalendarEvents(),newCalendarEvents);
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
    private CalendarEvent createNewEvent(CalendarEvent collidedEvent) throws OperationException {
        System.out.println("\nEvent to change: ");
        System.out.println(collidedEvent);
        System.out.println("New values: <date> <startTime> <endTime> " + collidedEvent.getName() + " " + collidedEvent.getNote());

        while (true) {
            System.out.print(">");
            String newInput = scanner.nextLine();

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
                newCalendarEvent = new CalendarEvent(collidedEvent.getName(),date, startTime, endTime,  collidedEvent.getNote());
            } catch (CalendarException e) {
                System.out.println(e.getMessage());
                continue;
            }

            Set<CalendarEvent> incompatibleEvents=xmlParser.getCalendar().checkIfAllEventAreCompatibleWithCalendar(newCalendarEvent);

            if(incompatibleEvents.isEmpty())
                return newCalendarEvent;


            StringBuilder descriptionBuilder=new StringBuilder();
            for (CalendarEvent incompatibleEvent : incompatibleEvents) {
                descriptionBuilder.append(incompatibleEvent);
                descriptionBuilder.append("\n");
            }

            throw new OperationException(newCalendarEvent + "\n is currently incompatible with:"+descriptionBuilder);
        }
    }

    //endregion
}
