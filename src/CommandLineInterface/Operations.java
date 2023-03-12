package CommandLineInterface;

import CommandLineInterface.Parsers.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Operations<T extends FileParser> {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final T fileParser;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Operations(T fileParser) {
        this.fileParser = fileParser;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void open(String fileDirectory){
        if(fileParser.getFile() !=null) {
            System.out.println("There is currently opened file:" + fileParser.getFile().getAbsolutePath());
            return;
        }

        try {
            fileParser.setFileContent(fileParser.readFile(fileDirectory));
            System.out.println("File successfully opened:"+fileDirectory);

            if(fileParser.getFileContent()==null)
                System.out.println("File is empty");

            fileParser.setFile(new File(fileDirectory));
        }
        catch (IOException e) {
            System.out.println("File not found.\nCreating new file "+fileDirectory);

            if(!fileParser.createFileIfNotExist(fileDirectory))
            {
                System.out.println("New file cannot be created: "+fileDirectory);
                return ;
            }

            fileParser.setFileContent(new ArrayList<>());
        }
        catch (Exception ignored) {

        }
    }

    public void close(){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return ;
        }

        fileParser.setFileContent(null);
        System.out.println("File successfully closed "+fileParser.getFile().getAbsolutePath());
        fileParser.setFile(null);
    }

    public void save(){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return;
        }

        if(!fileParser.writeFile(fileParser.getFileContent()))
        {
            System.out.println("File cannot be saved "+fileParser.getFile().getAbsolutePath());
            return ;
        }

        System.out.println("File successfully saved "+fileParser.getFile().getAbsolutePath());
    }

    public void saveAs(String newFileDirectory){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return;
        }

        String currentDirectory=fileParser.getFile().getAbsolutePath();

        fileParser.setFile(new File(newFileDirectory));

        if(!(fileParser.deleteFile(currentDirectory)&&(fileParser.writeFile(fileParser.getFileContent()))))
        {
                System.out.println("File cannot be saved as "+newFileDirectory);
                return;
        }

        System.out.println("File saved as "+fileParser.getFile().getAbsolutePath());
    }

    public String help(){
        return """             
                \tDefault commands:                                     Description:\s
                \t\t\topen <file directory>                                 opens <file>\s
                \t\t\tclose                                                 closes currently opened file\s
                \t\t\tsave                                                  saves the currently open file\s
                \t\t\tsaveas <file directory>                               saves the currently open file in <file>
                \t\t\thelp                                                  prints this information\s
                \t\t\texit                                                  exists the program\s
                """;
    }

    public void exit(){
        System.exit(0);
    }

    //Accessors/Mutators
    public T getFileParser() {
        return fileParser;
    }
}
