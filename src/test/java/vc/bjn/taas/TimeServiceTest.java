package vc.bjn.taas;

import static org.testng.Assert.*;

import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeServiceTest {

    private TimeService timeService;

    @BeforeMethod
    private void init() {
        timeService = new TimeServiceImpl();
    }

    @Test
    public void defaultZone() {
        final DateTime expected = new DateTime();
        final Map<String, Integer> actual = timeService.getCurrentTime(DateTimeZone.getDefault());

        assertEquals((int) actual.get("hours"), expected.getHourOfDay(), "hours");
        assertEquals((int) actual.get("minutes"), expected.getMinuteOfHour(), "minutes");
        assertEquals((int) actual.get("seconds"), expected.getSecondOfMinute(), "seconds");
    }

    @Test
    public void utc() {
        final DateTime expected = new DateTime(DateTimeZone.UTC);
        final Map<String, Integer> actual = timeService.getCurrentTime(DateTimeZone.forID("Etc/UTC"));

        assertEquals((int) actual.get("hours"), expected.getHourOfDay(), "hours");
        assertEquals((int) actual.get("minutes"), expected.getMinuteOfHour(), "minutes");
        assertEquals((int) actual.get("seconds"), expected.getSecondOfMinute(), "seconds");
    }

}
