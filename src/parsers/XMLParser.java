package parsers;

import contracts.FileParser;
import models.Calendar;
import models.CalendarEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Set;

public class XMLParser implements FileParser<CalendarEvent> {

    private File file;
    private Calendar calendar;

    public XMLParser() {
        this.calendar = new Calendar();
    }


    public void createFileIfNotExist(String path){
        Calendar newCalendar = new Calendar();
        try {
            JAXBContext context = JAXBContext.newInstance(Calendar.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(newCalendar, new File(path));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile(String path){
        File file=new File(path);
        return file.delete();
    }

    public void readFile(){
        try {
            JAXBContext jaxbContext=JAXBContext.newInstance(Calendar.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            calendar=(Calendar) unmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<CalendarEvent> readFile(String path){
        try {
            JAXBContext jaxbContext=JAXBContext.newInstance(Calendar.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Calendar calendar=(Calendar) unmarshaller.unmarshal(new File(path));

            return calendar.getCalendarEvents();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean writeFile(){
        try {
            JAXBContext jaxbContext=JAXBContext.newInstance(Calendar.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(calendar,file);

            return true;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    //region Getters and Setters

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile(){
        return file;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    //endregion
}
