package PersonalCalendar;

import java.util.ArrayList;
import java.util.List;


public class CalendarCommandLineHandler{


    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~
    private ArrayList<CalendarCommands> commands;
    private String command;
    private ArrayList<String> inputArray;
    private ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~
    public CalendarCommandLineHandler(ArrayList<CalendarCommands> commands) {
        this.commands = commands;
    }

    public void handleInput(String inputLine){

        if (inputLine.equals("") || inputLine.equals(" "))
            return;

        inputLine = inputLine.replace(" +", " ");

        inputArray = new ArrayList<>(List.of(inputLine.split(" ")));

        if (inputArray.size() == 0)
            return;

        command= inputArray.get(0);

        instructions=new ArrayList<>(inputArray.subList(1,inputArray.size()));

    }

   public void handleOperation(CalendarOperations operations){
       if(command==null) {
           instructions=null;
           return;
       }

       switch (command) {
           case "close" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 0)
                   System.out.println("'" + command + "' does not expect arguments");
               else
                   operations.close();
           }

           case "open" -> {
               if(operations.getFileParser().getFile() !=null) {
                   System.out.println("There is currently opened file:" + operations.getFileParser().getFile().getAbsolutePath());
                   command=null;
                   instructions=null;
                   return;
               }

               if (instructions.size() != 1)
                   System.out.println("'" + command + "' expects one argument");
               else
                   operations.open(instructions.get(0));
           }

           case "help" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 0)
                   System.out.println("'" + command + "' does not expect arguments");
               else
                   System.out.println(operations.help());
           }

           case "save" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 0)
                   System.out.println("'" + command + "' does not expect arguments");
               else
                   operations.save();
           }

           case "saveas" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 1)
                   System.out.println("'" + command + "' expects one argument");
               else
                   operations.saveAs(instructions.get(0));
           }

           case "exit" ->{
               if (instructions.size() != 0)
                   System.out.println("'" + command + "' does not expect arguments");
               else
                   operations.exit();
           }

           case "book" ->{
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() < 5)
                   System.out.println("'" + command + "' expects <date> <startTime> <endTime> <name> <note>");
               else
                   operations.book(instructions);
           }

           case "unbook" ->{
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 3)
                    System.out.println("'" + command + "' expects <date> <startTime> <endTime>");
               else
                    operations.unbook(instructions);
           }

           case "agenda" ->{
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 1)
                   System.out.println("'" + command + "' expects <date>");
               else
                   operations.agenda(instructions);
           }

            case "change" ->{
                if(operations.getFileParser().getFile() ==null) {
                    System.out.println("There is no currently opened file at the moment.");
                    command=null;
                    instructions=null;
                    return ;
                }

                if (instructions.size() < 4)
                    System.out.println("'" + command + "' expects <date> <startTime> <option> <newValue>");
                else
                    operations.change(instructions);
            }

           case "find" ->{
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 1)
                   System.out.println("'" + command + "' expects <string>");
               else
                   operations.find(instructions);
           }

           case "holiday" ->{
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 1)
                   System.out.println("'" + command + "' expects <date>");
               else
                   operations.holiday(instructions);
           }

           case "busydays" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 2)
                   System.out.println("'" + command + "' expects <from> <to>");
               else
                   operations.busydays(instructions);
           }

           case "findslot" -> {
               if(operations.getFileParser().getFile() ==null) {
                   System.out.println("There is no currently opened file at the moment.");
                   command=null;
                   instructions=null;
                   return ;
               }

               if (instructions.size() != 2)
                   System.out.println("'" + command + "' expects <fromDate> <hours>");
               else
                   operations.findslot(instructions);
           }

           default -> System.out.println(command+" is not recognized as an internal command!");
       }
       command=null;
       instructions=null;
   }

}
