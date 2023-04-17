package cli.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;

import java.io.File;
import java.util.ArrayList;

public class SaveAs implements Operation {
    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;
    private final ArrayList<String> instructions;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public SaveAs(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser=fileParser;
        this.instructions=instructions;

    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        String newFileDirectory=instructions.get(0);

        String currentDirectory=fileParser.getFile().getAbsolutePath();

        fileParser.setFile(new File(newFileDirectory));

        if(!(fileParser.deleteFile(currentDirectory)&&(fileParser.writeFile(fileParser.getFileContent()))))
        {
            System.out.println("File cannot be saved as "+newFileDirectory);
            return false;
        }

        System.out.println("File saved as "+fileParser.getFile().getAbsolutePath());
        return true;
    }
}
