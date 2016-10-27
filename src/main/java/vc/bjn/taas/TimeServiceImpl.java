package vc.bjn.taas;

import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimeServiceImpl implements TimeService {

    @Override
    public Map<String, Integer> getCurrentTime(final DateTimeZone timezone) {
        final DateTime currentTime = new DateTime(timezone);
        final Map<String, Integer> result = new HashMap<>();

        result.put(HOURS, currentTime.getHourOfDay());
        result.put(MINUTES, currentTime.getMinuteOfHour());
        result.put(SECONDS, currentTime.getSecondOfMinute());
        return result;
    }

}
