package farasense.mobile.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class DateUtil {

    public static Date getNow() {
        return new Date();
    }

    public static DateTimeZone getTimeZoneBrazil() { return DateTimeZone.forOffsetHours(-3); }

    public static List<Interval> getAllIntervalsLast24Hours() {
        DateTime startHour = new DateTime().withZone(DateTimeZone.forOffsetHours(-3));
        DateTime endHour = new DateTime().withZone(DateTimeZone.forOffsetHours(-3));
        List<Interval> intervalos = new ArrayList<>();

        int fimDaHora = 0;
        for (int inicioDaHora = 1; inicioDaHora < 25; inicioDaHora++) {
            Interval interval = new Interval(startHour.minusHours(inicioDaHora), endHour.minusHours(fimDaHora));
            intervalos.add(interval);
            fimDaHora++;
        }

        return intervalos;
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

    public static Date getFirstMomentOfTheDay() {
        DateTime dateTime = new DateTime()
                .withZone(DateUtil.getTimeZoneBrazil())
                .withHourOfDay(1)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        return dateTime.toDate();
    }

    public static Date getFirstMomentOfTheHour(int hourOfDay) {
        DateTime dateTime = new DateTime()
                .withZone(DateUtil.getTimeZoneBrazil())
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        return dateTime.toDate();
    }

    public static Date getLastMomentOfTheHour(int hourOfDay) {
        DateTime dateTime = new DateTime()
                .withZone(DateUtil.getTimeZoneBrazil())
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(59)
                .withSecondOfMinute(59)
                .withMillisOfSecond(999);

        return dateTime.toDate();
    }

//    public static int getIntervalDate() {
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        DateTime start = new DateTime(year, 1, 1, 0, 0, 0, 0);
//
//        return Days.daysBetween(start, new DateTime()).getDays();
//    }

}
