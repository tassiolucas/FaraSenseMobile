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

    public static final int FIVE_MINUTES = 5;
    public static int THIRTY_MINUTES = 30;

    public static DateTimeZone getTimeZoneBrazil() { return DateTimeZone.forOffsetHours(-3); }

    // Funções principais relativas ao tempo
    public static Date getNow() {
        return new DateTime().withZone(getTimeZoneBrazil()).toDate();
    }

    public static Date getFirts24Hours() {
        DateTime firts24Hours = new DateTime().withZone(getTimeZoneBrazil()).minusHours(24);
        return firts24Hours.toDate();
    }

    public static Date getFirts24HoursWithMinutesReset() {
        DateTime firts24Hours = new DateTime().withZone(getTimeZoneBrazil()).minusHours(24);
        firts24Hours = firts24Hours.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0);
        return firts24Hours.toDate();
    }

    public static Date getFirts30Days() {
        DateTime firts30Days = new DateTime().withZone(getTimeZoneBrazil()).minusDays(30);
        firts30Days = firts30Days.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0);
        return firts30Days.toDate();
    }

    public static Date getFirts12Monthly() {
        DateTime firts30Days = new DateTime().withZone(getTimeZoneBrazil()).minusMonths(12);
        firts30Days = firts30Days.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0);
        return firts30Days.toDate();
    }

    public static Date getFirstMomentOfTheDay() {
        DateTime dateTime = new DateTime().withZone(DateUtil.getTimeZoneBrazil())
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        return dateTime.toDate();
    }

    public static List<Interval> getAllIntervalsLast12Monthly() {
        DateTime startDay = new DateTime().withZone(getTimeZoneBrazil()).withDayOfMonth(0).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime endDay = new DateTime().withZone(getTimeZoneBrazil()).withDayOfMonth(31).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        List<Interval> intervals = new ArrayList<>();

        int endMonthly = 0;
        for (int beginningMonthly = 1; beginningMonthly < 12; beginningMonthly++) {
            Interval interval = new Interval(startDay.minusMonths(beginningMonthly), endDay.minusMonths(endMonthly));
            intervals.add(interval);
            endMonthly++;
        }

        return intervals;
    }

    public static List<Interval> getAllIntervalsLast30Days() {
        DateTime startDay = new DateTime().withZone(getTimeZoneBrazil()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime endDay = new DateTime().withZone(getTimeZoneBrazil()).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        List<Interval> intervals = new ArrayList<>();

        int endOfTheDay = 0;
        for (int beginningDay = 1; beginningDay < 30; beginningDay++) {
            Interval interval = new Interval(startDay.minusDays(beginningDay), endDay.minusDays(endOfTheDay));
            intervals.add(interval);
            endOfTheDay++;
        }

        return intervals;
    }

    // Funções para os gráficos relativos ao tempo
    public static List<Interval> getAllIntervalsLast24Hours() {
        DateTime startHour = new DateTime().withZone(getTimeZoneBrazil());
        DateTime endHour = new DateTime().withZone(getTimeZoneBrazil());
        List<Interval> intervals = new ArrayList<>();

        int fimDaHora = 0;
        for (int inicioDaHora = 1; inicioDaHora < 25; inicioDaHora++) {
            Interval interval = new Interval(startHour.minusHours(inicioDaHora), endHour.minusHours(fimDaHora));
            intervals.add(interval);
            fimDaHora++;
        }

        // Retorna intervalos das últimas 24 horas (de hora em hora) (início da hora e fim da hora)
        return intervals;
    }

    public static List<Interval> getAllIntervalsLast12Hours() {
        DateTime startThirty = new DateTime().withZone(getTimeZoneBrazil());
        DateTime endThirty = new DateTime().withZone(getTimeZoneBrazil());
        List<Interval> intervals = new ArrayList<>();

        int start30Min = 0;
        int end30Min = THIRTY_MINUTES;
        for (int count = 1; count < 25; count++) {
            Interval interval = new Interval(startThirty.minusMinutes(end30Min), endThirty.minusMinutes(start30Min));
            intervals.add(interval);
            start30Min = start30Min + THIRTY_MINUTES;
            end30Min = end30Min + THIRTY_MINUTES;
        }

        return intervals;
    }

    public static List<Interval> getAllIntervalsLast2Hours() {
        DateTime startFive = new DateTime().withZone(getTimeZoneBrazil());
        DateTime endFive = new DateTime().withZone(getTimeZoneBrazil());
        List<Interval> intervals = new ArrayList<>();

        int start5Min = 0;
        int end5Min = FIVE_MINUTES;
        for (int count = 1; count < 25; count++) {
            Interval interval = new Interval(startFive.minusMinutes(end5Min), endFive.minusMinutes(start5Min));
            intervals.add(interval);
            start5Min = start5Min + FIVE_MINUTES;
            end5Min = end5Min + FIVE_MINUTES;
        }

        return intervals;
    }

    // TODO: LIMPAR FUNCIONALIDADE DE FERRAMENTAS AO TERMINAR O DESENVOLVIMENTO (RETIRAR OS MÉTODOS INÚTEIS)
    // Funções alternativas
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

}
