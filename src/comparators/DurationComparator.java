package comparators;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.Comparator;
import java.util.*;

/**
 * A Comparator implementation that compares Map.Entry objects containing a DayOfWeek key and a Duration value based on the value (Duration) in ascending order.
 */
public class DurationComparator implements Comparator<Map.Entry<DayOfWeek,Duration>> {

    /**
     * Compares two Map.Entry objects containing a DayOfWeek key and a Duration value based on the value (Duration) in ascending order.
     * @param o1 the first Map.Entry object to be compared
     * @param o2 the second Map.Entry object to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second, respectively.
     */
    @Override
    public int compare(Map.Entry<DayOfWeek,Duration> o1, Map.Entry<DayOfWeek,Duration> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
