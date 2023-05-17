package project.contracts;

import project.exceptions.OperationException;
import project.models.calendar.PersonalCalendar;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Set;


/**
 * The FileParser interface represents a generic file parser for reading and writing files.
 * The generic type T represents the type of object being parsed from the file.
 */
public interface FileParser<T>{

    /**
     * Reads a file from the given path and returns a set of parsed objects.
     * @param path the path of the file to read
     * @return a set of parsed objects
     * @throws JAXBException if an error occurs while parsing the file
     */
    T readFile(File file) throws OperationException;

    /**
     * Writes the current state of the program to the currently open file.
     * @throws JAXBException if an error occurs while writing to the file
     */
    void writeFile(PersonalCalendar personalCalendar,File file) throws OperationException;

}
