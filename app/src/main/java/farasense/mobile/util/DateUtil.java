package farasense.mobile.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    static Calendar calendar;

    public static Date firstDayOfTheYear() {
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        calendar.set(Calendar.HOUR, 12);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        return calendar.getTime();
    }
}
