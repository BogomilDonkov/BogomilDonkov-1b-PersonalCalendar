package cli.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Open implements Operation {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~
    public Open(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser=fileParser;
        this.instructions=instructions;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {

        String fileDirectory=instructions.get(0);
        try {
            fileParser.setFileContent(fileParser.readFile(fileDirectory));

            if(fileParser.getFileContent()==null)
                System.out.println("File is empty");

            fileParser.setFile(new File(fileDirectory));
            System.out.println("File successfully opened:"+fileParser.getFile().getAbsolutePath());
            return true;
        }
        catch (IOException e) {
            System.out.println("File not found.\nCreating new file "+fileDirectory);

            if(!fileParser.createFileIfNotExist(fileDirectory))
            {
                System.out.println("New file cannot be created: "+fileDirectory);
                return false;
            }

            fileParser.setFileContent(new HashSet<>());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
