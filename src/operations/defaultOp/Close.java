package operations.defaultOp;

import contracts.Operation;
import parsers.XMLParser;

public class Close implements Operation<Void> {

    private final XMLParser fileParser;


    public Close(XMLParser fileParser) {
        this.fileParser=fileParser;
    }


    @Override
    public  Void execute() {
        System.out.println("File successfully closed "+fileParser.getFile().getAbsolutePath());
        fileParser.setCalendar(null);
        fileParser.setFile(null);
        return null;
    }
}
