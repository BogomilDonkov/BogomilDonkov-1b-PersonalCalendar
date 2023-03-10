package CommandLineInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Operations<T extends XMLParser> {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private ArrayList<Object> fileContent=null;
    //private String currentlyFilePath=null;
    private T fileParser;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Operations(T fileParser) {
        this.fileParser = fileParser;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public boolean open(String fileDirectory){
        if(fileParser.getFile() !=null)
           System.out.println("There is currently opened file:"+fileParser.getFile().getAbsolutePath());

        try {
            fileContent = fileParser.readFile(fileDirectory);
            System.out.println("File successfully opened:"+fileDirectory);

            if(fileContent==null)
                System.out.println("File is empty");

            fileParser.setFile(new File(fileDirectory));

            //currentlyFilePath=fileDirectory;
            return true;

        }
        catch (IOException e) {
            System.out.println("File not found.\nCreating new file "+fileDirectory);

            if(!fileParser.createFileIfNotExist(fileDirectory))
            {
                System.out.println("New file cannot be created: "+fileDirectory);
                return false;
            }

            fileContent =new ArrayList<>();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean close(){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return false;
        }

        fileContent =null;
        System.out.println("File successfully closed "+fileParser.getFile().getAbsolutePath());
        fileParser.setFile(null);

        return true;
    }

    public boolean save(){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return false;
        }

        //fileParser.setFile(new File(currentlyFilePath));

        if(!fileParser.writeFile(fileContent))
        {
            System.out.println("File cannot be saved "+fileParser.getFile().getAbsolutePath());
            return false;
        }

        System.out.println("File successfully saved "+fileParser.getFile().getAbsolutePath());
        return true;
    }

    public boolean saveAs(String newFileDirectory){
        if(fileParser.getFile() ==null) {
            System.out.println("There is no currently opened file at the moment.");
            return false;
        }

        String currentDirectory=fileParser.getFile().getAbsolutePath();

        fileParser.setFile(new File(newFileDirectory));

        if(!(fileParser.writeFile(fileContent)&&fileParser.deleteFile(currentDirectory)))
        {
                System.out.println("File cannot be saved as "+newFileDirectory);
                return false;
        }

        System.out.println("File saved as "+fileParser.getFile().getAbsolutePath());

        return true;
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

    public ArrayList<Object> getFileContent() {
        return fileContent;
    }
}
