package operations.defaultOp;

import contracts.DefaultOperation;
import parsers.XMLParser;

/**
 * The Close class implements the DefaultOperation interface and represents the operation
 * of closing a calendar file.
 */
public class Close implements DefaultOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final XMLParser xmlParser;

    /**
     * Constructs a Close object with the provided XMLParser object.
     * @param xmlParser The XMLParser object that will be used to parse the calendar file.
     */
    public Close(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    /**
     * Closes the calendar file associated with the XMLParser object and sets the calendar and file to null.
     */
    @Override
    public  void execute() {
        System.out.println("File successfully closed "+ xmlParser.getFile().getAbsolutePath());
        xmlParser.setCalendar(null);
        xmlParser.setFile(null);
    }
}
