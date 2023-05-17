package project.models.parsers;

import project.contracts.FileParser;
import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;
import project.models.calendar.CalendarEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Set;

/**
 * The XMLParser class is responsible for parsing and manipulating CalendarEvents stored in an XML file.
 * This class implements the FileParser interface, and therefore is responsible for reading and writing files.
 */
public final class XMLParser implements FileParser<PersonalCalendar> {

    /**
     * Creates a new XML file if it does not already exist.
     * @param path the path of the file to create.
     * @throws JAXBException if there is an error creating the file.
     */
    public void createFileIfNotExist(PersonalCalendar personalCalendar,File file) throws OperationException {
        try{
        JAXBContext context = JAXBContext.newInstance(PersonalCalendar.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(personalCalendar,file);
        } catch (JAXBException ignored) {
            throw new OperationException("New file cannot be created");
        }
    }

    /**
     * Deletes the specified file.
     * @param path the path of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteFile(File file){
        return file.delete();
    }


    /**
     * Reads the specified XML file and returns a set of CalendarEvents contained in the file.
     * @param path the path of the file to read.
     * @return a set of CalendarEvents contained in the file.
     * @throws JAXBException if there is an error reading the file.
     */
    public PersonalCalendar readFile(File file) throws OperationException {
        PersonalCalendar personalCalendar;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonalCalendar.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            personalCalendar =(PersonalCalendar) unmarshaller.unmarshal(file);
        } catch (JAXBException ignored) {
            throw new OperationException("Cannot open: "+file.getAbsolutePath());
        }

        return personalCalendar;
    }

    /**
     * Writes the calendar object to the specified XML file.
     * @throws JAXBException if there is an error writing to the file.
     */
    public void writeFile(PersonalCalendar personalCalendar,File file) throws OperationException {

        try {
            JAXBContext jaxbContext=JAXBContext.newInstance(PersonalCalendar.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(personalCalendar,file);
        } catch (JAXBException ignored) {
            throw new OperationException("File cannot be saved "+ file.getAbsolutePath());
        }

    }
}
