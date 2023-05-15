package project.models.operations.userDefault;

import project.contracts.DefaultOperation;
import project.models.operations.Commands;

/**
 * A class that displays the list of default commands and their descriptions.
 */
public class Help implements DefaultOperation {

    /**
     * Displays the list of default commands and their descriptions.
     */
    @Override
    public void execute() {
        StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("Default commands:\tArguments:\t\t\t\t\t\t\t\t\tDescription:\n");

        for (Commands commands : Commands.values()) {
            buildDescription(helpBuilder, commands.getName(), commands.getInstructions(), commands.getDescription(), commands);
        }

        System.out.println(helpBuilder);
    }

    /**
     * Appends the command name, instructions, and description to the provided string builder.
     *
     * @param builder      The string builder to append the command information to.
     * @param name         The name of the command.
     * @param instructions The instructions for using the command.
     * @param description  The description of what the command does.
     * @param commands     The command being processed.
     */
    //region Internal Methods
    private void buildDescription(StringBuilder builder, String name, String instructions, String description, Commands commands) {
        builder.append("\t");
        builder.append(String.format("%-15s", name));
        builder.append("\t");
        builder.append(String.format("%-45s", instructions));
        builder.append("\t");

        if (description.length() < 80) {
            builder.append(description).append("\n");
        } else {
            String[] formatedDescription = description.split("(?<=\\.)\\s");
            builder.append(formatedDescription[0]).append("\n");
            for (int i = 1; i < formatedDescription.length; i++) {
                String str = " ".repeat(72);
                builder.append(str).append(formatedDescription[i]).append("\n");
            }
        }
    }

    //endregion
}
