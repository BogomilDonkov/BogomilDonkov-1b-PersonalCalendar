package enums;

public enum Commands {

    //region Default Operations
    OPEN("open","<file>","Opens <file>."),
    CLOSE("close"," ","Closes currently opened file."),
    SAVE("save"," ","Saves the currently open file."),
    SAVEAS("saveas","<file>","saves the currently open file in <file>."),
    HELP("help"," ","Prints this information."),
    EXIT("exit"," ","Exists the program."),

    //endregion

    //region Calendar Operations
    BOOK("book","<date> <startTime> <endTime> <name> <note>","Books an event using given arguments."),
    UNBOOK("unbook","<date> <startTime> <endTime>","Unbooks an event using given arguments."),
    AGENDA("agenda","<date>","Prints all events for given date in chronological order."),
    CHANGE("change","<date> <startTime> <option> <newValue>", "Select the event you want to update with <date> and <startTime>. <option> can take: date,startTime,endTime,name or note as argument. With <newValue> we update the value to the current <option>."),
    FIND("find","<string>","Prints all events that contains the given string in their name or note."),
    HOLIDAY("holiday","<date>","The date <date> is marked as non-working."),
    BUSYDAYS("busydays","<from> <to>","Prints all working days within given time range, sorted by the number of occupied hours criteria."),
    FINDSLOT("findslot","<fromDate> <hours>","Looks for a free meeting place on a given date and desired duration of meeting. Аs the meeting must be booked only on working days and in the range from 08:00 to 17:00."),
    FINDSLOTWITH("findslotwith","<fromDate> <hours> <calendar>","'Findslotwith' is implementation of 'Findslot' that supports multiple calendars."),
    MERGE("merge","<calendar>","Merge given calendar with the one that already loaded in the program. Supports multiple calendars.");

    //endregion

    private final String name;
    private final String instructions;
    private final String description;

    Commands(String name, String instructions, String description){
        this.name=name;
        this.instructions=instructions;
        this.description=description;
    }


    public String getName() {
        return name;
    }
    public String getInstructions() {
        return instructions;
    }
    public String getDescription() {
        return description;
    }
}
