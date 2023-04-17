package cli.operations;

import cli.interfaces.Operation;

public class Exit implements Operation {
    //region Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean execute() {
        System.out.println("Exiting program...");
        return false;
    }
    //endregion
}
