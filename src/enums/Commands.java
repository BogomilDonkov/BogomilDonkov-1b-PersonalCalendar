package enums;

/**
 * This enum class contains all the commands supported by the calendar program.
 * Each command has a name, a set of instructions to use it, and a description.
 */
public enum Commands {

    //region Default Operations
    /**
     * Command to open a file.
     */
    OPEN("open","<file>","Opens <file>."),
    /**
     * Command to close the currently opened file.
     */
    CLOSE("close"," ","Closes currently opened file."),
    /**
     * Command to save the currently open file.
     */
    SAVE("save"," ","Saves the currently open file."),
    /**
     * Command to save the currently open file in a new file.
     */
    SAVEAS("saveas","<file>","saves the currently open file in <file>."),
    /**
     * Command to print all the available commands and their descriptions.
     */
    HELP("help"," ","Prints this information."),

    /**
     * Command to exit the program.
     */
    EXIT("exit"," ","Exists the program."),

    //endregion

    //region Calendar Operations
    /**
     * Command to book an event in the calendar.
     */
    BOOK("book","<date> <startTime> <endTime> <name> <note>","Books an event using given arguments."),
    /**
     * Command to unbook an event from the calendar.
     */
    UNBOOK("unbook","<date> <startTime> <endTime>","Unbooks an event using given arguments."),
    /**
     * Command to print all events for a given date in chronological order.
     */
    AGENDA("agenda","<date>","Prints all events for given date in chronological order."),
    /**
     * Command to update an event in the calendar.
     */
    CHANGE("change","<date> <startTime> <option> <newValue>", "Select the event you want to update with <date> and <startTime>. <option> can take: date,startTime,endTime,name or note as argument. With <newValue> we update the value to the current <option>."),
    /**
     * Command to search for events containing a given string in their name or note.
     */
    FIND("find","<string>","Prints all events that contains the given string in their name or note."),
    /**
     * Command to mark a date as non-working (holiday).
     */
    HOLIDAY("holiday","<date>","The date <date> is marked as non-working."),
    /**
     * Command to print all working days within a given time range, sorted by the number of occupied hours.
     */
    BUSYDAYS("busydays","<from> <to>","Prints all working days within given time range, sorted by the number of occupied hours criteria."),
    /**
     * Command to find a free meeting place on a given date and desired duration of meeting.
     */
    FINDSLOT("findslot","<fromDate> <hours>","Looks for a free meeting place on a given date and desired duration of meeting. Аs the meeting must be booked only on working days and in the range from 08:00 to 17:00."),
    /**
     * Command to find a free meeting place on a given date and desired duration of meeting, considering multiple calendars.
     */
    FINDSLOTWITH("findslotwith","<fromDate> <hours> <calendar>","'Findslotwith' is implementation of 'Findslot' that supports multiple calendars."),

    /**
     Command to merge a given calendar with the one that already loaded in the program. Supports multiple calendars.
     */
    MERGE("merge","<calendar>","Merge given calendar with the one that already loaded in the program. Supports multiple calendars.");

    //endregion

    /**
     * Name of the command
     */
    private final String name;

    /**
     * Instructions of the command
     */
    private final String instructions;

    /**
     * Description of the command
     */
    private final String description;

    /**
     Constructs a new command with the given name, instructions and description.
     @param name the name of the command
     @param instructions the usage instructions for the command
     @param description the description of the command
     */
    Commands(String name, String instructions, String description){
        this.name=name;
        this.instructions=instructions;
        this.description=description;
    }

    /**
     * Returns the name of the command.
     * @return the name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the usage instructions for the command.
     * @return the usage instructions for the command
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Returns the description of the command.
     * @return the description of the command
     */
    public String getDescription() {
        return description;
    }
}
