package CommandLineInterface;

import java.util.ArrayList;
import java.util.List;

public class CommandLineHandler {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~
    private ArrayList<Commands> commands;
    private Commands command;

    ArrayList<String> inputArray;
    private ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~
    public CommandLineHandler(ArrayList<Commands> commands) {
        this.commands = commands;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void handleInput(String inputLine){

        if (inputLine.equals("") || inputLine.equals(" "))
            return;

        inputLine = inputLine.replace(" +", " ");

        inputArray = new ArrayList<>(List.of(inputLine.split(" ")));

        if (inputArray.size() == 0)
            return;


        if (inputArray.size() > 6) {
            System.out.println("The console supports up to 5 arguments");
            return;
        }

        command= Commands.valueOf(inputArray.get(0));
        instructions=new ArrayList<>(inputArray.subList(1,inputArray.size()));

    }

    public void handleOperation(Operations operations){
        switch (command) {
            case close -> {
                if (instructions.size() != 0)
                    System.out.println("'close' does not expect arguments");
                else
                    operations.close();
            }

            case open -> {
                if (instructions.size() != 1)
                    System.out.println("'open' expects one argument");
                else
                    operations.open(instructions.get(0));
            }

            case help -> {
                if (instructions.size() != 0)
                    System.out.println("'help' does not expect arguments");
                else
                    System.out.println(operations.help());
            }

            case save -> {
                if (instructions.size() != 0)
                    System.out.println("'save' does not expect arguments");
                else
                    operations.save();
            }

            case saveas -> {
                if (instructions.size() != 1)
                    System.out.println("'saveas' expects one argument");
                 else
                    operations.saveAs(instructions.get(0));
            }

            case exit ->{
                if (instructions.size() != 0)
                    System.out.println("'exit' does not expect arguments");
                else
                    operations.exit();
            }

            default -> System.out.println(command+" is not recognized as an internal command!");
        }
    }

    public Commands getCommand() {
        return command;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }
}
