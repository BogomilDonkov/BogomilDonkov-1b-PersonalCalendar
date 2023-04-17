package cli.interfaces;

//import cli.Operations;
import cli.parsers.XMLParser;

import java.util.ArrayList;

public interface Operation {
    //region Methods
    <T> T execute();
    //endregion
}
