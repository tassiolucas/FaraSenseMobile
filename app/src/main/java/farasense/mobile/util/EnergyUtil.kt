package farasense.mobile.util

import org.joda.time.DateTime
import org.joda.time.Interval
import java.util.Date
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO

object EnergyUtil {

    const val EMPTY_MEASURE = 0.0

    // TODO: Verificar a relação entre horas e o corte do servidor

    fun getKwhInPeriod(totalPowerMeasure: Double?, quantMeasure: Int, startDate: Date, endDate: Date): Double? {
        val HOUR_MINUTES = 60
        val KILO = 1000

        val mediaPowerMeasure = totalPowerMeasure!! / quantMeasure

        val startPeriod = DateTime(startDate).withZone(DateUtil.timeZoneBrazil)
        val endPeriod = DateTime(endDate).withZone(DateUtil.timeZoneBrazil)

        val interval = Interval(startPeriod, endPeriod)

        val hour = java.lang.Double.valueOf(interval.toDuration().standardMinutes.toDouble()) / HOUR_MINUTES

        val wh = mediaPowerMeasure * hour

        return wh / KILO
    }

    fun getValueCost(maturityDate: DateTime, rateKhw: Float?, rateFlag: Float? = 0F): Double? {
        var totalCost: Double?
        val startPeriod = DateUtil.get30DaysAgo(maturityDate)

        val faraSenseSensorDailyList = FaraSenseSensorDailyDAO().getMeasureByIntervals(startPeriod.toDate(), maturityDate.toDate())

        var measure: Double? = 0.0
        for (faraSenseSensorDaily in faraSenseSensorDailyList) {
            measure = measure!! + faraSenseSensorDaily.totalKwh!!
        }

        totalCost = measure!! * rateKhw!!
        totalCost += rateFlag!!

        return totalCost
    }
}
