package project.util;

import java.util.Scanner;

/**
 * Scanner class.
 */
public final class CalendarScanner {

    /**
     * A static final Scanner object used to read input from the console.
     */
    private static final Scanner scanner=new Scanner(System.in);

    /**
     * A private constructor because we don't want initializations of this class.
     */
    private CalendarScanner(){}

    /**
     * The scan function of the scanner
     * @return string line of the console input
     */
    public static String scanNextLine(){
        return scanner.nextLine();
    }
}
