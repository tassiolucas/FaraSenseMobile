package farasense.mobile.view_model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList
import java.util.Collections
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO
import farasense.mobile.model.DAO.FaraSenseSensorHoursDAO
import farasense.mobile.util.DateUtil

class DailyChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val dayChartLabels = ArrayList<String>()

    val dailyConsumpition: List<BarEntry>
        get() {
            var daysBehind = 0
            val entriesMeasures = ArrayList<BarEntry>()
            val measureList = ArrayList<Double>()
            val intervals30Days = DateUtil.allIntervalsLast30Days

            do {
                val sensorMeasuresList = FaraSenseSensorDailyDAO.getMeasureByIntervals(
                        intervals30Days[daysBehind].start.toDate(),
                        intervals30Days[daysBehind].end.toDate()
                )

                if (sensorMeasuresList != null) {
                    var totalKwh: Double? = 0.0
                    for (measure in sensorMeasuresList) {
                        totalKwh = totalKwh!! + measure.totalKwh!!
                    }

                    totalKwh?.let { measureList.add(it) }
                } else {
                    measureList.add(java.lang.Double.valueOf(0.0))
                }

                dayChartLabels.add(intervals30Days[daysBehind].end.dayOfMonth.toString() + "/" + intervals30Days[daysBehind].end.monthOfYear.toString())

                daysBehind++
            } while (daysBehind < intervals30Days.size)

            val todayMeauseres = FaraSenseSensorHoursDAO.getDailyComsumption()

            var todayTotalKwh: Double? = 0.0
            if (todayMeauseres != null) {
                for (todayMeausere in todayMeauseres) {
                    todayTotalKwh = todayTotalKwh!! + todayMeausere.kwh!!
                }
            }
            todayTotalKwh?.let { measureList.set(FIRST_MEASURE, it) }

            measureList.reverse()

            var i = 0
            for (measure in measureList) {
                entriesMeasures.add(i, BarEntry(
                        java.lang.Float.valueOf(i.toFloat()),
                        java.lang.Float.valueOf(measure.toFloat())
                ))
                i++
            }

            return entriesMeasures
        }

    val dailyChartLabels: List<String>
        get() {
            dayChartLabels.reverse()
            return dayChartLabels
        }

    companion object {
        const val FIRST_MEASURE = 0
    }

}
