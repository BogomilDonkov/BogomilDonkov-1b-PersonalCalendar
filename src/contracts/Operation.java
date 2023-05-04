package contracts;

import exceptions.CalendarException;

/**
 * The Operation interface represents an operation that can be executed in the program.
 * Any class that implements this interface must implement the execute() method,
 * which performs the operation and throws an OperationException if an error occurs.
 */
public interface Operation {

   /**
    * Executes the operation.
    * @throws CalendarException if an error occurs while executing the operation.
    */
   void execute() throws CalendarException;

}
