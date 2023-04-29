package operations.defaultOp;

import contracts.Operation;
import parsers.XMLParser;

import java.util.ArrayList;

public class Save implements Operation<Boolean> {

    private final XMLParser xmlParser;

    public Save(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }


    @Override
    public Boolean execute() {
        if(!xmlParser.writeFile())
        {
            System.out.println("File cannot be saved "+ xmlParser.getFile().getAbsolutePath());
            return false;
        }

        ArrayList<String> mergedCalendars=xmlParser.getCalendar().getMergedCalendars();

        if(!mergedCalendars.isEmpty()) {
            for(String calendarName:mergedCalendars){
                xmlParser.deleteFile(calendarName);
            }
        }

        System.out.println("File successfully saved "+ xmlParser.getFile().getAbsolutePath());
        return true;
    }
}
