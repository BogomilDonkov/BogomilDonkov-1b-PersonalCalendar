package cli.interfaces;

import personalCalendar.models.CalendarEvent;

import java.io.File;
import java.util.HashSet;

public interface FileParser {
    //region Methods
    boolean createFileIfNotExist(String path);
    boolean deleteFile(String path);
    HashSet<CalendarEvent> readFile(String path) throws Exception;
    boolean writeFile(HashSet<CalendarEvent> content);
    HashSet<CalendarEvent> getFileContent();
    void setFileContent(HashSet<CalendarEvent> fileContent);
    void setFile(File file);
    File getFile();
    //endregion
}
