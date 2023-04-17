package cli.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;

public class Close implements Operation {

    //region Members
    private final FileParser fileParser;
    //endregion

    //region Constructors
    public Close(FileParser fileParser) {
        this.fileParser=fileParser;
    }
    //endregion

    //region Methods
    @Override
    public boolean execute() {
        System.out.println("File successfully closed "+fileParser.getFile().getAbsolutePath());
        fileParser.setFileContent(null);
        fileParser.setFile(null);
        return true;
    }
    //endregion
}
