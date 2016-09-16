package estimote.com.estimotetest.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class ISO8601DateTime {

    /**
     * Transform Calendar to ISO 8601 string.
     *
     * @param calendar
     * @return
     */
    public static String fromCalendar(@NonNull final Calendar calendar) {
        Date date = calendar.getTime();
        return fromDate(date);
    }

    /**
     * Transform Date to ISO 8601 string.
     *
     * @param date
     * @return
     */
    public static String fromDate(@NonNull final Date date) {
        if (date != null) {
            String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
            return formatted.substring(0, 22) + ":" + formatted.substring(22);
        }
        return null;
    }

    /**
     * Get current date and time formatted as ISO 8601 string.
     *
     * @return
     */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    public static String getTodayStartTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 7);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return fromCalendar(instance);
    }

    /**
     * Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string
     * @return
     * @throws ParseException
     */
    public static Calendar toCalendar(@NonNull final String iso8601string) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

}