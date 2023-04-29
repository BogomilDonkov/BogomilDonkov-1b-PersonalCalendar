package operations.defaultOp;

import contracts.Operation;
import parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;

public class Open implements Operation<Void> {

    private final XMLParser fileParser;
    private final ArrayList<String> instructions;


    public Open(XMLParser fileParser, ArrayList<String> instructions) {
        this.fileParser=fileParser;
        this.instructions=instructions;
    }


    @Override
    public Void execute() {
        String fileDirectory = instructions.get(0);
        fileParser.setFile(new File(fileDirectory));

        if(fileParser.getFile().exists()){
            fileParser.readFile();
            System.out.println("File successfully opened: " + fileDirectory);

            if (fileParser.getCalendar().isEmpty())
                System.out.println("File is empty.");

            return null;
        }
        else {
            fileParser.createFileIfNotExist(fileDirectory);
            System.out.println("File not found.\nNew file was created: " + fileDirectory);
        }

        return execute();
    }
}
