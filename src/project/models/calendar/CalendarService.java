package project.models.calendar;

import project.contracts.FileParser;
import project.exceptions.OperationException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * A service for importing and exporting calendar data to and from files.
 */
public class CalendarService {

    /**
     * The file that is currently loaded into the service. This file will be used for importing and exporting calendar data.
     */
    private File loadedFile;

    /**
     * The repository where calendar data is stored. The service will import data from the loaded file into this repository,
     * and export data from the repository to the loaded file.
     */
    private PersonalCalendar repository;

    /**
     * A FileParser object for importing and exporting calendar data. This is a generic class that takes a PersonalCalendar type
     * parameter, which indicates the type of data that will be parsed from files. The parser object will be used to read and
     * write data to and from the loaded file.
     */
    private FileParser<PersonalCalendar> parser;

    /**
     * A service for importing and exporting calendar data to and from files.
     */
    public CalendarService(PersonalCalendar repository, FileParser<PersonalCalendar> parser) {
        this.repository = repository;
        this.parser = parser;
    }

    /**
     * Imports calendar data from the loaded file into the repository.
     * @throws OperationException If there is an error importing the data.
     */
    public void importToRepository() throws OperationException {
        repository = parser.readFile(loadedFile);
    }

    /**
     * Exports calendar data from the repository to the loaded file.
     * @throws OperationException If there is an error exporting the data.
     */
    public void exportFromRepository() throws OperationException {
        parser.writeFile(repository,loadedFile);
    }

    /**
     * Creates the loaded file if it does not already exist and writes the calendar data to it.
     * @throws OperationException If there is an error creating or writing to the file.
     */
    public void createFileIfNotExist() throws OperationException {
        parser.createFileIfNotExist(repository,loadedFile);
    }

    /**
     * Deletes the specified file.
     * @param filename The name of the file to delete.
     * @return true if the file was deleted successfully, otherwise false.
     */
    public boolean deleteFile(String filename){
        return parser.deleteFile(new File(filename));
    }

    //region Setters and Getters

    /**
     * Gets the currently loaded file.
     * @return The loaded file.
     */
    public File getLoadedFile() {
        return loadedFile;
    }

    /**
     * Sets the loaded file.
     * @param loadedFile The file to load.
     */
    public void setLoadedFile(File loadedFile) {
        this.loadedFile = loadedFile;
    }

    /**
     * Gets the repository.
     * @return The repository.
     */
    public PersonalCalendar getRepository() {
        return repository;
    }

    /**
     * Sets the repository.
     * @param repository The repository to use.
     */
    public void setRepository(PersonalCalendar repository) {
        this.repository = repository;
    }

    /**
     * Gets the file parser.
     * @return The file parser.
     */
    public FileParser<PersonalCalendar> getParser() {
        return parser;
    }

    //endregion
}
