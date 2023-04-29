package operations.calendarOp;

import contracts.CalendarOperation;
import exceptions.CalendarException;
import exceptions.OperationException;
import models.Calendar;
import models.CalendarEvent;
import parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.util.*;

import static cli.CalendarCLI.scanner;

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
     * Executes the merge operation. It reads events from the files in the instruction list, checks for collisions with the events
     * already present in the calendar and merges them if there are no collisions or the user chooses to edit the conflicting events.
     * @throws OperationException If there is an error reading the input files or editing conflicting events.
     */
    @Override
    public void execute() throws OperationException {
        Calendar calendar = xmlParser.getCalendar();

        for(String fileName:instructions){

            Set<CalendarEvent> newCalendarEvents;
            try {
                newCalendarEvents = new HashSet<>(xmlParser.readFile(fileName));
            }catch (JAXBException e)
            {
                throw new OperationException("Cannot open file: "+fileName);
            }

            Map<CalendarEvent, CalendarEvent> collisionMap = new HashMap<>(checkCollidedEvents(calendar.getCalendarEvents(),newCalendarEvents));

            if (!collisionMap.isEmpty()) {
                System.out.println("There is collision between events: ");

                for (Map.Entry<CalendarEvent, CalendarEvent> entry : collisionMap.entrySet())
                    System.out.println(entry.getKey() + " from " + xmlParser.getFile().getName() + " collided with:\n" + entry.getValue() + " from " + fileName);

                System.out.println("""
                                               
                        If you want to proceed, you need to edit your events.
                        Do you want to proceed ?  (Press 'Y' to accept. Press any other key to cancel.)\s""");
                System.out.print(">");

                String option = scanner.nextLine();

               if(option.equals("Y") || option.equals("y")) {

                   for (CalendarEvent event : collisionMap.values()) {
                       System.out.println("\nEvent to change: ");
                       System.out.println(event);
                       System.out.println("New values: <date> <startTime> <endTime> " + event.getName() + " " + event.getNote());

                       calendar.addEvent(createNewEvent(event));
                   }
                   calendar.addMergedCalendar(fileName);
               }
               else {
                   System.out.println(fileName + " was not merged into " + xmlParser.getFile().getName());
               }
                continue;
            }

            calendar.addAll(newCalendarEvents);
            calendar.addMergedCalendar(fileName);
        }

        if(!xmlParser.getCalendar().getMergedCalendars().isEmpty())
            System.out.println("All calendars: were successfully merged to " + xmlParser.getFile().getName() + "!");
    }


    //region Internal Methods

    /**
     * A helper method that checks for collided events between two sets of events.
     * @param firstCalendarEvents the events from the current calendar
     * @param secondCalendarEvents the events from the given calendar
     * @return a map of the collided events
     */
    private Map<CalendarEvent, CalendarEvent> checkCollidedEvents(Set<CalendarEvent> firstCalendarEvents, Set<CalendarEvent> secondCalendarEvents){
        Map<CalendarEvent, CalendarEvent> collidedEvents = new HashMap<>();

        for (CalendarEvent firstCalendarEvent : firstCalendarEvents)
            for (CalendarEvent secondCalendarEvent : secondCalendarEvents)
                if (!secondCalendarEvent.checkCompatibility(firstCalendarEvent))
                    collidedEvents.put(firstCalendarEvent, secondCalendarEvent);
        return collidedEvents;
    }

    /**
     * Creates a new calendar event based on the given old event, with user input for the date, start time, and end time.
     * @param oldEvent the old calendar event to base the new event on
     * @return the new calendar event
     * @throws OperationException if the new calendar event is incompatible with the existing events in the calendar
     */
    private CalendarEvent createNewEvent(CalendarEvent oldEvent) throws OperationException {
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
                newCalendarEvent = new CalendarEvent(oldEvent.getName(),date, startTime, endTime,  oldEvent.getNote());
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
