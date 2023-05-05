package models.operations.userDefault;

import contracts.DefaultOperation;
import parsers.XMLParser;

/**
 * A class representing the "Exit" command, which is used to exit the program.
 */
public class Exit implements DefaultOperation {

    private XMLParser xmlParser;

    public Exit(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    /**
     * Executes the "Exit" command by printing a message indicating that the program is exiting.
     */
    @Override
    public void execute() {
        xmlParser.setCalendar(null);
        xmlParser.setFile(null);
        xmlParser=null;
        System.gc();
        System.exit(0);
    }
}
