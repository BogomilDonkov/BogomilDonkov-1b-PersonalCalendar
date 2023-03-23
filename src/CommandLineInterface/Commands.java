package CommandLineInterface;

public enum Commands {
    HELP,
    OPEN,
    CLOSE,
    SAVE,
    SAVEAS,
    EXIT;

    @Override
    public String toString() {
        return this.toString().toLowerCase();
    }
}

