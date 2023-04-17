package cli.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;

public class Save implements Operation {

    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final FileParser fileParser;

    //Constructors~~~~~~~~~~~~~~~~~~~~~~~~~
    public Save(FileParser fileParser) {
        this.fileParser=fileParser;
    }

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        if(!fileParser.writeFile(fileParser.getFileContent()))
        {
            System.out.println("File cannot be saved "+fileParser.getFile().getAbsolutePath());
            return false;
        }

        System.out.println("File successfully saved "+fileParser.getFile().getAbsolutePath());
        return true;
    }
}
