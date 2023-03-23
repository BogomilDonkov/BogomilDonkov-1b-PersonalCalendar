package PersonalCalendar;

public enum CalendarCommands{

    BOOK,
    UNBOOK,
    AGENDA,
    CHANGE,
    FIND,
    HOLIDAY,
    BUSYDAYS,
    FINDSLOT,
    FINDSLOTWITH,
    MERGE;

    @Override
    public String toString() {
        return this.toString().toLowerCase() ;
    }
}
