package parsers;

import contracts.FileParser;
import models.calendar.PersonalCalendar;
import models.calendar.CalendarEvent;

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
public class XMLParser implements FileParser<CalendarEvent> {

    /**
     * The file to be parsed.
     */
    private File file;

    /**
     * The {@link PersonalCalendar} object that contains the parsed CalendarEvents.
     */
    private PersonalCalendar personalCalendar;

    /**
     * Constructs a {@link XMLParser} object with the provided {@link XMLParser} and instruction list.
     * @param personalCalendar The {@link PersonalCalendar} object that will be used to parse the calendar.
     */
    public XMLParser(PersonalCalendar personalCalendar) {
        this.personalCalendar = personalCalendar;
    }

    /**
     * Creates a new XML file if it does not already exist.
     * @param path the path of the file to create.
     * @throws JAXBException if there is an error creating the file.
     */
    public void createFileIfNotExist(String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PersonalCalendar.class);

        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(personalCalendar, new File(path));
    }

    /**
     * Deletes the specified file.
     * @param path the path of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteFile(String path){
        File file=new File(path);
        return file.delete();
    }

    /**
     * Reads the XML file and populates the calendar object with the parsed CalendarEvents.
     * @throws JAXBException if there is an error reading the file.
     */
    public void readFile() throws JAXBException {

        JAXBContext jaxbContext=JAXBContext.newInstance(PersonalCalendar.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        personalCalendar =(PersonalCalendar) unmarshaller.unmarshal(file);
    }


    /**
     * Reads the specified XML file and returns a set of CalendarEvents contained in the file.
     * @param path the path of the file to read.
     * @return a set of CalendarEvents contained in the file.
     * @throws JAXBException if there is an error reading the file.
     */
    public Set<CalendarEvent> readFile(String path) throws JAXBException {

        JAXBContext jaxbContext=JAXBContext.newInstance(PersonalCalendar.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        PersonalCalendar personalCalendar =(PersonalCalendar) unmarshaller.unmarshal(new File(path));

        return personalCalendar.getCalendarEvents();
    }

    /**
     * Writes the calendar object to the specified XML file.
     * @throws JAXBException if there is an error writing to the file.
     */
    public void writeFile() throws JAXBException {

        JAXBContext jaxbContext=JAXBContext.newInstance(PersonalCalendar.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(personalCalendar,file);
    }


    //region Getters and Setters

    /**
     * Sets the file to be parsed.
     * @param file the file to be parsed.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns the file to be parsed.
     * @return the file to be parsed.
     */
    public File getFile(){
        return file;
    }

    /**
     * Returns the Calendar object containing the parsed CalendarEvents.
     * @return the Calendar object containing the parsed CalendarEvents.
     */
    public PersonalCalendar getCalendar() {
        return personalCalendar;
    }

    /**
     * Sets the Calendar
     * @param personalCalendar The calendar to be parsed
     */
    public void setCalendar(PersonalCalendar personalCalendar) {
        this.personalCalendar = personalCalendar;
    }

    //endregion
}
