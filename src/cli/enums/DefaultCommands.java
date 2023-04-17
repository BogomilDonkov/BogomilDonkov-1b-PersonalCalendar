package cli.enums;

public enum DefaultCommands {
    //region Default Enums
    OPEN("open","<file>"),
    CLOSE("close"," "),
    SAVE("save"," "),
    SAVEAS("saveas","<file>"),
    HELP("help"," "),
    EXIT("exit"," "),
    //endregion

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
    DefaultCommands(String name, String instructions){
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

