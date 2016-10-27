package vc.bjn.taas;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTimeZone;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeServletTest {

    @InjectMocks private TimeServlet timeServlet;
    @Mock private TimeService timeService;

    @BeforeMethod
    private void init() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(timeServlet, "timeService", timeService);
    }

    @Test
    public void getCurrentTimeSpecificZone() throws ServletException, IOException {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("timezone")).thenReturn("America/New_York");

        final TestPrintWriter writer = new TestPrintWriter();
        when(resp.getWriter()).thenReturn(writer);
        assertFalse(writer.isClosed());

        final Map<String, Integer> expected = new HashMap<>();
        expected.put("hours", 1);
        expected.put("minutes", 2);
        expected.put("seconds", 3);
        when(timeService.getCurrentTime(any(DateTimeZone.class))).thenReturn(expected);

        timeServlet.doGet(req, resp);

        final String actual = writer.toString();
        assertTrue(actual.contains("\"hours\":1"));
        assertTrue(actual.contains("\"minutes\":2"));
        assertTrue(actual.contains("\"seconds\":3"));
        assertTrue(writer.isClosed(), "should have closed writer");

        verify(resp).setContentType("application/json");
        verify(resp).addHeader("Access-Control-Allow-Origin", "*");
        verify(resp).addHeader("Cache-Control", "no-cache, must-revalidate");
        verify(timeService).getCurrentTime(DateTimeZone.forID("America/New_York"));
    }

    @Test
    public void getCurrentTimeDefaultZone() throws ServletException, IOException {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("timezone")).thenReturn(null);

        final TestPrintWriter writer = new TestPrintWriter();
        when(resp.getWriter()).thenReturn(writer);

        final Map<String, Integer> expected = new HashMap<>();
        expected.put("hours", 1);
        expected.put("minutes", 2);
        expected.put("seconds", 3);
        when(timeService.getCurrentTime(any(DateTimeZone.class))).thenReturn(expected);

        timeServlet.doGet(req, resp);

        final String actual = writer.toString();
        assertTrue(actual.contains("\"hours\":1"));
        assertTrue(actual.contains("\"minutes\":2"));
        assertTrue(actual.contains("\"seconds\":3"));
        assertTrue(writer.isClosed(), "should have closed writer");

        verify(resp).setContentType("application/json");
        verify(resp).addHeader("Access-Control-Allow-Origin", "*");
        verify(resp).addHeader("Cache-Control", "no-cache, must-revalidate");
        verify(timeService).getCurrentTime(any(DateTimeZone.class));
    }

    @Test
    public void getCurrentTimeInvalidZone() throws ServletException, IOException {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        final HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("timezone")).thenReturn("hargle-blarg");

        final TestPrintWriter writer = new TestPrintWriter();
        when(resp.getWriter()).thenReturn(writer);

        timeServlet.doGet(req, resp);

        final String actual = writer.toString();
        assertTrue(actual.startsWith("{\"error\":\"unknown timezone ID "));
        assertTrue(writer.isClosed(), "should have closed writer");

        verify(resp).setContentType("application/json");
        verify(resp).addHeader("Access-Control-Allow-Origin", "*");
        verify(resp).addHeader("Cache-Control", "no-cache, must-revalidate");
        verify(timeService, never()).getCurrentTime(any(DateTimeZone.class));
    }

    private static final class TestPrintWriter extends PrintWriter {
        private final StringBuilder stringBuilder = new StringBuilder();
        private boolean isClosed = false;

        public TestPrintWriter() {
            super(System.out);
        }

        @Override
        public void write(final int c) {
            stringBuilder.append((char) c);
        }

        @Override
        public void write(final String s) {
            stringBuilder.append(s);
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }

        @Override
        public void close() {
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

    }
}
