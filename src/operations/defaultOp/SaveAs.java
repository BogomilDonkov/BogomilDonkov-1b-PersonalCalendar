package operations.defaultOp;

import contracts.Operation;
import parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveAs implements Operation<Boolean> {

    private final XMLParser xmlParser;
    private final ArrayList<String> instructions;


    public SaveAs(XMLParser xmlParser, ArrayList<String> instructions) {
        this.xmlParser = xmlParser;
        this.instructions=instructions;
    }


    private boolean checkIfFileAlreadyExistsAndSubmitUserResponse(String newFileDirectory){
        if(new File(newFileDirectory).exists())
        {
            System.out.println("There is "+ newFileDirectory+ " that currently exists.\n" +
                    "Do you want to replace it ? Press 'N' to cancel or press any other key to proceed.");

            String option=new Scanner(System.in).nextLine();

            if(option.equals("N")||option.equals("n")){
                return true;
            }
        }
        return true;
    }


    @Override
    public Boolean execute() {
        String newFileDirectory=instructions.get(0);

        if(checkIfFileAlreadyExistsAndSubmitUserResponse(newFileDirectory))
            return false;

        String currentDirectory= xmlParser.getFile().getAbsolutePath();

        xmlParser.setFile(new File(newFileDirectory));

        if(!((xmlParser.writeFile())&& xmlParser.deleteFile(currentDirectory)))
        {
            System.out.println("File cannot be saved as "+newFileDirectory);
            return false;
        }

        ArrayList<String> mergedCalendars=xmlParser.getCalendar().getMergedCalendars();

        if(!mergedCalendars.isEmpty()) {
            for(String calendarName:mergedCalendars){
                xmlParser.deleteFile(calendarName);
            }
        }

        System.out.println("File saved as "+ xmlParser.getFile().getAbsolutePath());
        return true;
    }
}
