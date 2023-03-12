package CommandLineInterface.Parsers;

import java.io.File;
import java.util.ArrayList;

public interface FileParser {

    boolean createFileIfNotExist(String path);
    boolean deleteFile(String path);
    ArrayList<Object> readFile(String path) throws Exception;
    boolean writeFile(ArrayList<Object> content);

    ArrayList<Object> getFileContent();

    void setFileContent(ArrayList<Object> fileContent);

    void setFile(File file);
    File getFile();
}
