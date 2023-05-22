package project.contracts;

import project.exceptions.OperationException;

/**
 * Interface for a command line interface.
 * @param <E> the type of elements handled by the command line interface
 */
public interface CommandLineInterface<E> {
    /**
        Runs the command line interface.
    */
    void run();
}
