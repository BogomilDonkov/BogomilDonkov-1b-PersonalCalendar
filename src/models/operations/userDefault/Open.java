package models.operations.userDefault;

import contracts.DefaultOperation;
import exceptions.OperationException;
import models.calendar.PersonalCalendar;
import parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;

/**
 * The Open class represents an operation for opening an XML file containing a calendar.
 */
public class Open implements DefaultOperation {

    /**
     * The XMLParser object that will be used to parse the calendar.
     */
    private final XMLParser xmlParser;

    /**
     * The ArrayList containing the instructions for the operation.
     */
    private final ArrayList<String> instructions;

    /**
     * Constructs a Open object with the provided XMLParser and instruction list.
     * @param xmlParser The XMLParser object that will be used to parse the calendar.
     * @param instructions The ArrayList containing the instructions for the operation.
     */
    public Open(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions=instructions;
    }

    /**
     * Executes the Open operation, opening the XML file containing the calendar.
     * @throws OperationException If an error occurs while opening the file.
     */
    @Override
    public void execute() throws OperationException {
        String fileDirectory = instructions.get(0);

        if(!fileDirectory.endsWith(".xml"))
            fileDirectory+=".xml";

        xmlParser.setFile(new File(fileDirectory));
        xmlParser.setCalendar(new PersonalCalendar());

        if(xmlParser.getFile().exists()) {

            try {
                xmlParser.readFile();
            } catch (JAXBException e)
            {
                throw new OperationException("File cannot be opened.");
            }

            System.out.println("File successfully opened: " + fileDirectory);

            if (xmlParser.getCalendar().isEmpty())
                System.out.println("File is empty.");

            return ;
        }

        System.out.println("File not found.");

        try {
            xmlParser.createFileIfNotExist(fileDirectory);
        } catch (JAXBException e) {
            throw new OperationException("New file cannot be created");
        }

        System.out.println("New file was created and loaded: " + fileDirectory);
    }
}
