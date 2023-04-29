package comparators;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.Comparator;
import java.util.*;

public class CalendarComparator implements Comparator<Map.Entry<DayOfWeek,Duration>> {
    @Override
    public int compare(Map.Entry<DayOfWeek,Duration> o1, Map.Entry<DayOfWeek,Duration> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
