package vc.bjn.taas;

import java.util.Map;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;

public interface TimeService {

    String HOURS = DurationFieldType.hours().getName();
    String MINUTES = DurationFieldType.minutes().getName();
    String SECONDS = DurationFieldType.seconds().getName();

    Map<String, Integer> getCurrentTime(DateTimeZone timezone);

}
