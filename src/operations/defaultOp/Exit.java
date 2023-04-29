package operations.defaultOp;

import contracts.Operation;

public class Exit implements Operation<Boolean> {

    @Override
    public Boolean execute() {
        System.out.println("Exiting program...");
        return false;
    }
}
