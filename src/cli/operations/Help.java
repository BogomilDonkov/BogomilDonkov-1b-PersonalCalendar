package cli.operations;

import cli.interfaces.Operation;

public class Help implements Operation {
    @Override
    public <T> T execute() {
        return null;
    }
    //region Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //public boolean execute() {
        //StringBuilder builder=new StringBuilder();
        //for(Commands command:Commands.values()){
        //    builder.append(command.getName());
        //    builder.append(" ");
        //    builder.append(command.getInstructions());
        //    builder.append("\n");
        //}

        //System.out.println(builder);

        System.out.println("""
                \tFile commands:                                     Description:\s
                \t\t\topen <file directory>                                 opens <file>\s
                \t\t\tclose                                                 closes currently opened file\s
                \t\t\tsave                                                  saves the currently open file\s
                \t\t\tsaveas <file directory>                               saves the currently open file in <file>
                \t\t\thelp                                                  prints this information\s
                \t\t\texit                                                  exists the program\s
                \tCalendar commands:\s
                \t\t\tbook <date> <startTime> <endTime> <name> <note>       Books an event using given arguments.\s
                \t\t\tunbook <date> <startTime> <endTime>                   Unbooks an event using given arguments.\s
                \t\t\tagenda <date>                                         Prints all events for given date in chronological order.\s
                \t\t\tchange <date> <startTime> <option> <newValue>         Select the event you want to update with <date> and <startTime>.\s
                \t\t\t                                                      <option> takes : date,startTime,endTime,name or note as argument.\s
                \t\t\t                                                      With <newValue> we update the value to the current <option>.\s
                \t\t\tfind <string>                                         Prints all events that contains the given string in their name or note.\s
                \t\t\tholiday                                               ---------\s
                \t\t\tbusydays                                              ---------\s
                \t\t\tfindslot                                              ---------\s
                \t\t\tfindslotwith                                          ---------\s
                \t\t\tmerge                                                 ---------\s
                """);

        return true;
    }


    //endregion
}
