package factories;

import contracts.Operation;
import enums.*;
import exceptions.OperationException;
import parsers.XMLParser;
import operations.defaultOp.*;
import operations.calendarOp.*;

import java.util.ArrayList;

public class OperationFactory{

    private final XMLParser xmlParser;


    public OperationFactory(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }



    public Operation<?> getOperation(Commands command, ArrayList<String> instructions) throws OperationException {

        if (checkInstructionsLength(instructions,command))
            return null;

        if(checkIfFileIsLoaded()) {
            switch (command) {
                case EXIT -> { return new Exit(); }
                case HELP -> { return new Help(); }
                case CLOSE -> { return new Close(xmlParser); }
                case SAVE -> { return new Save(xmlParser); }
                case SAVEAS -> { return new SaveAs(xmlParser, instructions); }
                case OPEN -> {
                    System.out.println("There is currently opened file:" + xmlParser.getFile().getAbsolutePath());
                    return null;
                }
                case BOOK -> { return new Book(xmlParser.getCalendar(), instructions); }
                case UNBOOK -> { return new Unbook(xmlParser.getCalendar(), instructions); }
                case AGENDA -> { return new Agenda(xmlParser.getCalendar(), instructions); }
                case CHANGE -> { return new Change(xmlParser.getCalendar(), instructions); }
                case FIND -> { return new Find(xmlParser.getCalendar(), instructions); }
                case HOLIDAY -> { return new Holiday(xmlParser.getCalendar(), instructions); }
                case BUSYDAYS -> { return new Busydays(xmlParser.getCalendar(), instructions); }
                case FINDSLOT -> { return new FindSlot(xmlParser.getCalendar(), instructions); }
                case FINDSLOTWITH -> { return new FindSlotWith(xmlParser, instructions); }
                case MERGE -> { return new Merge(xmlParser, instructions); }
                default -> throw new OperationException("Current operation not found!");
            }
        }
        else {
            switch (command) {
                case EXIT -> { return new Exit(); }
                case HELP -> { return new Help(); }
                case OPEN -> { return new Open(xmlParser, instructions); }
                default -> throw new OperationException("There is no currently opened file at the moment.");
            }
        }
    }


    //region Internal Methods

    private boolean checkIfFileIsLoaded(){
        return xmlParser.getFile() != null;
    }
    private boolean checkInstructionsLength(ArrayList<String> instructions, Commands command){
        int defaultInstructionsSize=command.getInstructions().split(" ").length;

        //Имаме едно единствено изключение ако командата ни е BOOK
        //Тъй като note може да съдържа в себе си повече от една дума, дължината на подадените инструкции може да надвишава зададената дължина по подразбиране
        if(command== Commands.BOOK) {
            if(instructions.size() < defaultInstructionsSize) {
                System.out.println("'" + command + "' expects " + command.getInstructions());
                return true;
            }
            else
                return false;
        }

        //Проверяваме дължината на подадените инструкции дали съвпада с дължината на инструкциите които изисква подадената команда
        if (instructions.size() != defaultInstructionsSize ) {

            if(command.getInstructions().equals(""))
                System.out.println("'" + command + "' does not expects arguments.");
            else
                System.out.println("'" + command + "' expects "+command.getInstructions());

            return true;
        }
        return false;
    }

    //endregion
}
