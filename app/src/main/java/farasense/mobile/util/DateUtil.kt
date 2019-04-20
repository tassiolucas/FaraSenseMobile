package farasense.mobile.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import java.util.*

object DateUtil {

    const val FIVE_MINUTES = 5
    const val THIRTY_MINUTES = 30

    val timeZoneBrazil: DateTimeZone
        get() = DateTimeZone.forOffsetHours(-3)

    // Funções principais relativas ao tempo
    val now: Date
        get() = DateTime().withZone(timeZoneBrazil).toDate()

    val firts24Hours: Date
        get() {
            val firts24Hours = DateTime().withZone(timeZoneBrazil).minusHours(24)
            return firts24Hours.toDate()
        }

    val firts24HoursWithMinutesReset: Date
        get() {
            var firts24Hours = DateTime().withZone(timeZoneBrazil).minusHours(24)
            firts24Hours = firts24Hours.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0)
            return firts24Hours.toDate()
        }

    val firts30Days: Date
        get() {
            var firts30Days = DateTime().withZone(timeZoneBrazil).minusDays(30)
            firts30Days = firts30Days.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0)
            return firts30Days.toDate()
        }

    val firts12Monthly: Date
        get() {
            var firts30Days = DateTime().withZone(timeZoneBrazil).minusMonths(12)
            firts30Days = firts30Days.withHourOfDay(0).withMinuteOfHour(0).withMillisOfDay(0)
            return firts30Days.toDate()
        }

    val firstMomentOfTheDay: Date
        get() {
            val dateTime = DateTime().withZone(DateUtil.timeZoneBrazil)
                    .withHourOfDay(0)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0)
                    .withMillisOfSecond(0)

            return dateTime.toDate()
        }

    val allIntervalsLast12Monthly: List<Interval>
        get() {
            val startDay = DateTime().withZone(timeZoneBrazil).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            val endDay = DateTime().withZone(timeZoneBrazil).withDayOfMonth(DateTime().dayOfMonth().withMaximumValue().dayOfMonth).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
            val intervals = ArrayList<Interval>()

            for (indexMonthly in 0..12) {
                val interval = Interval(startDay.minusMonths(indexMonthly), endDay.minusMonths(indexMonthly))
                intervals.add(interval)
            }

            return intervals
        }

    val allIntervalsLast10Year: List<Interval>
        get() {
            val startDay = DateTime().withZone(timeZoneBrazil).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            val endDay = DateTime().withZone(timeZoneBrazil).withMonthOfYear(12).withDayOfMonth(30).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
            val intervals = ArrayList<Interval>()

            for (indexYear in 0..9) {
                val interval = Interval(startDay.minusYears(indexYear), endDay.minusYears(indexYear))
                intervals.add(interval)
            }

            return intervals
        }

    val allIntervalsLast30Days: List<Interval>
        get() {
            val startDay = DateTime().withZone(timeZoneBrazil).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            val endDay = DateTime().withZone(timeZoneBrazil).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
            val intervals = ArrayList<Interval>()

            var endOfTheDay = 0
            for (beginningDay in 1..29) {
                val interval = Interval(startDay.minusDays(beginningDay), endDay.minusDays(endOfTheDay))
                intervals.add(interval)
                endOfTheDay++
            }

            return intervals
        }

    // Funções para os gráficos relativos ao tempo
    // Retorna intervalos das últimas 24 horas (de hora em hora) (início da hora e fim da hora)
    val allIntervalsLast24Hours: List<Interval>
        get() {
            val startHour = DateTime().withZone(timeZoneBrazil)
            val endHour = DateTime().withZone(timeZoneBrazil)
            val intervals = ArrayList<Interval>()

            var fimDaHora = 0
            for (inicioDaHora in 1..24) {
                val interval = Interval(startHour.minusHours(inicioDaHora), endHour.minusHours(fimDaHora))
                intervals.add(interval)
                fimDaHora++
            }
            return intervals
        }

    val allIntervalsLast12Hours: List<Interval>
        get() {
            val startThirty = DateTime().withZone(timeZoneBrazil)
            val endThirty = DateTime().withZone(timeZoneBrazil)
            val intervals = ArrayList<Interval>()

            var start30Min = 0
            var end30Min = THIRTY_MINUTES
            for (count in 1..24) {
                val interval = Interval(startThirty.minusMinutes(end30Min), endThirty.minusMinutes(start30Min))
                intervals.add(interval)
                start30Min = start30Min + THIRTY_MINUTES
                end30Min = end30Min + THIRTY_MINUTES
            }

            return intervals
        }

    val allIntervalsLast2Hours: List<Interval>
        get() {
            val startFive = DateTime().withZone(timeZoneBrazil)
            val endFive = DateTime().withZone(timeZoneBrazil)
            val intervals = ArrayList<Interval>()

            var start5Min = 0
            var end5Min = FIVE_MINUTES
            for (count in 1..24) {
                val interval = Interval(startFive.minusMinutes(end5Min), endFive.minusMinutes(start5Min))
                intervals.add(interval)
                start5Min = start5Min + FIVE_MINUTES
                end5Min = end5Min + FIVE_MINUTES
            }

            return intervals
        }

    // TODO: LIMPAR FUNCIONALIDADE DE FERRAMENTAS AO TERMINAR O DESENVOLVIMENTO (RETIRAR OS MÉTODOS INÚTEIS)
    // Funções alternativas
    val firtsThirtyDayInPast: Date
        get() {
            val today = Date()
            val cal = GregorianCalendar()
            cal.time = today
            cal.add(Calendar.DAY_OF_MONTH, -30)

            return cal.time
        }

    fun get30DaysAgo(selectedDay: DateTime): DateTime {
        return DateTime(selectedDay).minusMonths(1)
    }

    fun getDayInPast(day: Int): Date {
        val today = Date()
        val cal = GregorianCalendar()
        cal.time = today
        cal.add(Calendar.DAY_OF_MONTH, -day)

        return cal.time
    }


    fun getFirstMomentOfTheHour(hourOfDay: Int): Date {
        val dateTime = DateTime()
                .withZone(DateUtil.timeZoneBrazil)
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0)

        return dateTime.toDate()
    }

    fun getLastMomentOfTheHour(hourOfDay: Int): Date {
        val dateTime = DateTime()
                .withZone(DateUtil.timeZoneBrazil)
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(59)
                .withSecondOfMinute(59)
                .withMillisOfSecond(999)

        return dateTime.toDate()
    }
}
