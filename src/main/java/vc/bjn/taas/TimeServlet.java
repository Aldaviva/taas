package vc.bjn.taas;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;

@WebServlet("/now")
public class TimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TimeService timeService = new TimeServiceImpl();

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Cache-Control", "no-cache, must-revalidate");
        final PrintWriter respWriter = resp.getWriter();

        final String timezoneId = req.getParameter("timezone");
        final Map<?, ?> responseEntity = getTimeInZone(timezoneId);
        JSONObject.writeJSONString(responseEntity, respWriter);

        respWriter.close();
    }

    protected Map<?, ?> getTimeInZone(final String timezoneId) {
        try {
            final DateTimeZone timezone;
            if (timezoneId != null) {
                timezone = DateTimeZone.forID(timezoneId);
            } else {
                timezone = DateTimeZone.getDefault();
            }
            return timeService.getCurrentTime(timezone);
        } catch (final IllegalArgumentException e) {
            return Collections.singletonMap("error", "unknown timezone ID " + timezoneId
                    + ", make sure you are passing a valid IANA timezone ID such as America/New_York or Etc/UTC "
                    + "(see http://www.joda.org/joda-time/timezones.html)");
        }
    }

}
