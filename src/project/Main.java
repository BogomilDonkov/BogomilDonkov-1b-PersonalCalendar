package project;

import project.models.cli.CalendarCLI;

/**
 * The entry point of the calendar program.
 */
public class Main {
    /**
     * Private Constructor
     */
    private Main(){}
    /**
     * The main method that starts the calendar program by calling the CalendarCLI run method.
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {

        CalendarCLI.getInstance().run();
    }
}