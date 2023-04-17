package personalCalendar.enums;

public enum CalendarCommands {
    //region Calendar Enums
    BOOK("book","<date> <startTime> <endTime> <name> <note>"),
    UNBOOK("unbook","<date> <startTime> <endTime>"),
    AGENDA("agenda","<date>"),
    CHANGE("change","<date> <startTime> <option> <newValue>"),
    FIND("find","<string>"),
    HOLIDAY("holiday","<date>"),
    BUSYDAYS("busydays","<from> <to>"),
    FINDSLOT("findslot","<fromDate> <hours>"),
    FINDSLOTWITH("findslotwith","<fromDate> <hours> <calendar>"),
    MERGE("merge","<calendar>");
    //endregion

    //region Members
    private final String instructions;
    private final String name;
    //endregion

    //region Constructors
    CalendarCommands(String name, String instructions){
        this.instructions=instructions;
        this.name=name;
    }
    //endregion

    //region Methods
    public String getName() {
        return name;
    }
    public String getInstructions() {
        return instructions;
    }
    //endregion
}
