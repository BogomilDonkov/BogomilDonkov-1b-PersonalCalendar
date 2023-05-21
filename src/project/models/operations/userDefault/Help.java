package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.models.operations.Commands;

/**
 * A class that displays the list of default commands and their descriptions.
 */
public class Help implements DefaultOperation {

    /**
     * Default constructor
     */
    public Help(){}

    /**
     * Displays the list of default commands and their descriptions.
     */
    @Override
    public void execute() {
        StringBuilder helpBuilder=new StringBuilder();

        helpBuilder.append("Default commands:\tArguments:\t\t\t\t\t\t\t\t\tDescription:");

        for(Commands commands:Commands.values()){
            buildDescription(helpBuilder, commands.getName(), commands.getInstructions(), commands.getDescription(), commands);
        }

        System.out.println(helpBuilder);
    }

    /**
     * Appends the command name, instructions, and description to the provided string builder.
     * @param builder The string builder to append the command information to.
     * @param name The name of the command.
     * @param instructions The instructions for using the command.
     * @param description The description of what the command does.
     * @param commands The command being processed.
     */
    //region Internal Methods
    private void buildDescription(StringBuilder builder, String name, String instructions, String description, Enum<?> commands) {
        builder.append("\n\t");
        builder.append(String.format("%-15s", name));
        builder.append("\t");
        builder.append(String.format("%-45s", instructions));
        builder.append("\t");
        builder.append(description);
    }

    //endregion
}
