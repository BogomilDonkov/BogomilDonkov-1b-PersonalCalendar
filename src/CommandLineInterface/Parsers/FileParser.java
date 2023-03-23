package CommandLineInterface.Parsers;

import PersonalCalendar.CalendarEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public interface FileParser {

    boolean createFileIfNotExist(String path);
    boolean deleteFile(String path);
    HashSet<CalendarEvent> readFile(String path) throws Exception;
    boolean writeFile(HashSet<CalendarEvent> content);

    HashSet<CalendarEvent> getFileContent();

    void setFileContent(HashSet<CalendarEvent> fileContent);

    void setFile(File file);
    File getFile();
}
