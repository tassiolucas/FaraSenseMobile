package farasense.mobile.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

    public static Date getTodayDay() {
        return new Date();
    }

    public static Date getFirtsThirtyDayInPast() {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date firtDayOfTheYear = cal.getTime();

        return firtDayOfTheYear;
    }

    public static Date getDayInPast(int day) {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -day);
        Date firtDayOfTheYear = cal.getTime();

        return firtDayOfTheYear;
    }

    public static int getIntervalDate() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        DateTime start = new DateTime(year, 1, 1, 0, 0, 0, 0);

        return Days.daysBetween(start, new DateTime()).getDays();
    }

}
