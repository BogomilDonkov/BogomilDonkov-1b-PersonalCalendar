package contracts;

import java.util.Set;

public interface FileParser<T> {

    Set<T> readFile(String path);

    void readFile();

    boolean writeFile();

    void createFileIfNotExist(String path);

    boolean deleteFile(String path);
}
