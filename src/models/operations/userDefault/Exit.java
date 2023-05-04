package models.operations.userDefault;

import contracts.DefaultOperation;

/**
 * A class representing the "Exit" command, which is used to exit the program.
 */
public class Exit implements DefaultOperation {

    /**
     * Executes the "Exit" command by printing a message indicating that the program is exiting.
     */
    @Override
    public void execute() {
        System.out.println("Exiting program...");
    }
}
