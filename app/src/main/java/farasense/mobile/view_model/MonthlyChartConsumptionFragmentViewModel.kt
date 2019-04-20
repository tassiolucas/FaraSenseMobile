package farasense.mobile.view_model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO
import farasense.mobile.util.DateUtil

class MonthlyChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val monthlyChartLabels = ArrayList<String>()

    val monthlyConsumption: List<BarEntry>
        get() {
            var monthlyBehind = 0
            val entriesMeasures = ArrayList<BarEntry>()
            val measureList = ArrayList<Double>()
            val intervals12Monthly = DateUtil.allIntervalsLast12Monthly

            do {
                val sensorMeasuresList = FaraSenseSensorDailyDAO.getMeasureByIntervals(
                        intervals12Monthly[monthlyBehind].start.toDate(),
                        intervals12Monthly[monthlyBehind].end.toDate()
                )

                if (sensorMeasuresList != null) {
                    var totalKhm: Double? = 0.0
                    for (measure in sensorMeasuresList) {
                        totalKhm = totalKhm!! + measure.totalKwh!!
                    }

                    totalKhm?.let { measureList.add(it) }
                } else {
                    measureList.add(java.lang.Double.valueOf(0.0))
                }

                monthlyChartLabels.add(intervals12Monthly[monthlyBehind].start.monthOfYear.toString() + "/" + intervals12Monthly[monthlyBehind].start.year)

                monthlyBehind++
            } while (monthlyBehind < intervals12Monthly.size)

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

    fun getMonthlyChartLabels(): List<String> {
        monthlyChartLabels.reverse()
        return monthlyChartLabels
    }
}
