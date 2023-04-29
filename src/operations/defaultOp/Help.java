package operations.defaultOp;

import contracts.Operation;
import enums.Commands;


public class Help implements Operation<String> {

    @Override
    public String execute() {
        StringBuilder builder=new StringBuilder();

        builder.append("Default commands:\tArguments:\t\t\t\t\t\t\t\t\tDescription:");

        for(Commands commands:Commands.values()){
            buildDescription(builder, commands.getName(), commands.getInstructions(), commands.getDescription(), commands);
        }

        return builder.toString();
    }


    //region Internal Methods
    private void buildDescription(StringBuilder builder, String name, String instructions, String description, Enum<?> commands) {
        builder.append("\n\t");
        builder.append(String.format("%-15s", name));
        builder.append("\t");
        builder.append(String.format("%-45s", instructions));
        builder.append("\t");
        builder.append(description);
        builder.append("\n~~~~~~~~~~~~~~~~~~  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    //endregion
}
