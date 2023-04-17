package cli.factories;

import cli.operations.*;
import cli.enums.DefaultCommands;
import cli.exceptions.OperationException;
import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.operations.*;
import cli.parsers.XMLParser;

import java.util.ArrayList;

public class OperationFactory {
    //region Members
    private final FileParser fileParser;
    //endregion

    //region Constructors
    public OperationFactory(XMLParser fileParser) {
        this.fileParser = fileParser;
    }
    //endregion

    //region InternalMethods
    private boolean checkFile(){
        if (fileParser.getFile() == null) {
            System.out.println("There is no currently opened file at the moment.");
            return true;
        }
        return false;
    }
    private boolean checkInstructions(ArrayList<String> instructions, DefaultCommands command){
        int instructionsSize=command.getInstructions().split(" ").length;

        //Имаме едно единствено изключение ако командата ни е BOOK
        //Тъй като note може да съдържа в себе си повече от една дума, дължината на подадените инструкции може да надвишава зададената дължина по подразбиране
        if(command== DefaultCommands.BOOK) {
            if(instructions.size() < instructionsSize) {
                System.out.println("'" + command + "' expects " + command.getInstructions());
                return true;
            }
            else
                return false;
        }

        //Проверяваме дължината на подадените инструкции дали съвпада с дължината на инструкциите които изисква подадената команда
        if (instructions.size() != instructionsSize ) {

            if(command.getInstructions().equals(""))
                System.out.println("'" + command + "' does not expects arguments.");
            else
                System.out.println("'" + command + "' expects "+command.getInstructions());

            return true;
        }
        return false;
    }
    //endregion

    //region Methods
    public Operation getOperation(DefaultCommands command, ArrayList<String> instructions) throws OperationException {

        Operation operation;

        switch (command) {
            case EXIT -> operation = new Exit();
            case HELP -> operation = new Help();
            case CLOSE -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Close(fileParser);
            }
            case SAVE -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Save(fileParser);
            }
            case SAVEAS -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new SaveAs(fileParser, instructions);
            }
            case OPEN -> {
                if (fileParser.getFile() != null){
                    System.out.println("There is currently opened file:" + fileParser.getFile().getAbsolutePath());
                    return null;
                }

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Open(fileParser, instructions);
            }
            case BOOK -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;
                operation = new Book(fileParser, instructions);
            }
            case UNBOOK -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Unbook(fileParser, instructions);
            }
            case AGENDA -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Agenda(fileParser, instructions);
            }
            case CHANGE -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Change(fileParser, instructions);
            }
            case FIND -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Find(fileParser, instructions);
            }
            case HOLIDAY -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Holiday(fileParser, instructions);
            }
            case BUSYDAYS -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Busydays(fileParser, instructions);
            }
            case FINDSLOT -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new FindSlot(fileParser, instructions);
            }
            case FINDSLOTWITH -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new FindSlotWith(fileParser, instructions);
            }
            case MERGE -> {
                if (checkFile())
                    return null;

                if (checkInstructions(instructions,command))
                    return null;

                operation = new Merge(fileParser,instructions);
            }

            default -> throw new OperationException();
        }
        return operation;
    }
    //endregion
}
