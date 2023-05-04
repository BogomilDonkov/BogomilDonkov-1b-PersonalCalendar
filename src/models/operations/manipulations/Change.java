package models.operations.manipulations;

import contracts.CalendarOperation;
import exceptions.*;
import models.calendar.Calendar;
import models.calendar.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import static models.calendar.CalendarEvent.DATE_FORMATTER;
import static models.calendar.CalendarEvent.TIME_FORMATTER;

/**
 * The Change class implements the CalendarOperation interface and represents an operation to change an existing event in the calendar.
 * It takes in a Calendar object and a list of instructions for the operation.
 */
public class Change implements CalendarOperation {

    /**
     * The Calendar instance on which the operation will be executed.
     */
    private final Calendar calendar;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs an instance of the Change class with the specified Calendar and ArrayList of instructions.
     * @param calendar The Calendar instance on which the operation will be executed.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Change(Calendar calendar, ArrayList<String> instructions) {
        this.calendar = calendar;
        this.instructions = instructions;
    }

    /**
     * Executes the change operation by parsing the instructions and modifying the existing event in the calendar accordingly.
     * @throws OperationException if there is an error while executing the operation.
     */
    @Override
    public void execute() throws OperationException {
        String date=instructions.get(0);
        String startTime=instructions.get(1);
        String option=instructions.get(2);
        String newValue=instructions.get(3);

        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        try {
            CalendarEvent newEvent=new CalendarEvent(date,startTime);

            for(CalendarEvent oldEvent:calendarEvents)
            {
                if(oldEvent.equals(newEvent)) {
                    newEvent=new CalendarEvent(oldEvent.getName(),oldEvent.getDate(),oldEvent.getStartTime(),oldEvent.getEndTime(),oldEvent.getNote());

                    checkOptionAndGetDecision(option,newValue,newEvent,oldEvent);
                    return;
                }
            }
            throw new OperationException("There is no such event in the calendar.");

        } catch (CalendarDateException | CalendarTimeException | InvalidTimeIntervalException e) {
            throw new OperationException(e.getMessage());
        }
    }


    //region Internal Methods

    /**
     * Helper method that checks the specified option and gets the corresponding decision to update the event with the new value.
     * The new event is then checked for compatibility with the other events in the calendar and updated if
     * it is compatible, or an exception is thrown if it is not.
     * @param option The option to check.
     * @param newValue The new value to update.
     * @param newEvent The new event object to update.
     * @param oldEvent The old event object to update.
     * @throws InvalidTimeIntervalException if the time interval for the new event is invalid.
     * @throws OperationException if there is an error while executing the operation.
     */
    private void checkOptionAndGetDecision(String option,String newValue,CalendarEvent newEvent,CalendarEvent oldEvent) throws InvalidTimeIntervalException, OperationException {
        switch (option)
        {
            case "date"->{
                newEvent.setDate(LocalDate.parse(newValue, DATE_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            case "startTime" ->{
                newEvent.setStartTime(LocalTime.parse(newValue, TIME_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            case "endTime" ->{
                newEvent.setEndTime(LocalTime.parse(newValue, TIME_FORMATTER));
                checkAndUpdateCalendarEventSet(newEvent,oldEvent);
            }

            case "name" -> oldEvent.setName(newValue);

            case "note" ->{
                StringBuilder newValueBuilder = new StringBuilder(newValue);
                for(int i = 4; i<instructions.size(); i++)
                {
                    newValueBuilder.append(" ").append(instructions.get(i));
                }
                oldEvent.setNote(newValueBuilder.toString());
            }

            default -> throw new OperationException(option+" is not recognized as internal command.");
        }
    }


    /**
     * Checks if the new event is compatible with all the other events in the calendar, and updates the calendar if so.
     * If the new event is incompatible with any other event, throws an OperationException with a message describing the conflicts.
     * @param newEvent The CalendarEvent to be added to the calendar
     * @param oldEvent The CalendarEvent to be removed from the calendar
     * @throws OperationException If the new event is incompatible with any other event in the calendar
     */
    private void checkAndUpdateCalendarEventSet(CalendarEvent newEvent,CalendarEvent oldEvent) throws OperationException {
        boolean isCompatible=true;
        HashSet<CalendarEvent> incompatibleEvents = new HashSet<>();
        HashSet<CalendarEvent> calendarEvents=new HashSet<>(calendar.getCalendarEvents());

        for(CalendarEvent event:calendarEvents)
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
            calendar.remove(oldEvent);
            calendar.addEvent(newEvent);
        }
        else {
            StringBuilder descriptionBuilder=new StringBuilder();
            for(CalendarEvent event:incompatibleEvents){
                descriptionBuilder.append(event);
                descriptionBuilder.append("\n");
            }

            throw new OperationException("The event you have typed is currently incompatible with event\\s\n:"+descriptionBuilder.toString());
        }
    }

    //endregion
}
