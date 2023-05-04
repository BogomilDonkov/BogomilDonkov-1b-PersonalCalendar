package contracts;

import javax.xml.bind.JAXBException;
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
    Set<T> readFile(String path) throws JAXBException;

    /**
     * Reads the currently open file and returns a set of parsed objects.
     * @throws JAXBException if an error occurs while parsing the file
     */
    void readFile() throws JAXBException;

    /**
     * Writes the current state of the program to the currently open file.
     * @throws JAXBException if an error occurs while writing to the file
     */
    void writeFile() throws JAXBException;

    /**
     * Creates a new file with the given path if it does not exist.
     * @param path the path of the file to create
     * @throws JAXBException if an error occurs while creating the file
     */
    void createFileIfNotExist(String path) throws JAXBException;

    /**
     * Deletes the file at the given path.
     * @param path the path of the file to delete
     * @return true if the file was successfully deleted, false otherwise
     */
    boolean deleteFile(String path);
}
