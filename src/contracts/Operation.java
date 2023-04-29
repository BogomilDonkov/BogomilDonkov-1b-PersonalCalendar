package contracts;

import exceptions.OperationException;

/**
 * The Operation interface represents an operation that can be executed in the program.
 * Any class that implements this interface must implement the execute() method,
 * which performs the operation and throws an OperationException if an error occurs.
 */
public interface Operation {

   /**
    * Executes the operation.
    * @throws OperationException if an error occurs while executing the operation.
    */
   void execute() throws OperationException;

}
