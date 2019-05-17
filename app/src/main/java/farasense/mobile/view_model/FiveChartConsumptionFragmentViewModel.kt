package farasense.mobile.view_model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.Entry
import java.util.ArrayList
import farasense.mobile.model.DAO.FaraSenseSensorDAO
import farasense.mobile.util.DateUtil
import farasense.mobile.util.EnergyUtil

class FiveChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var fiveChartLabels: MutableList<String> = ArrayList()

    val hourPerFiveMinutes: List<Entry>
        get() {
            var indexIntervals = 0
            val entriesMeasures = ArrayList<Entry>()
            val measuresList = ArrayList<Double>()
            val intervalsFiveMinutes = DateUtil.allIntervalsLast2Hours
            fiveChartLabels = ArrayList()

            do {
                val sensorMeasureList = FaraSenseSensorDAO().getMeasureByIntervals(
                        intervalsFiveMinutes[indexIntervals].start.toDate(),
                        intervalsFiveMinutes[indexIntervals].end.toDate()
                )

                if (sensorMeasureList.isNotEmpty()) {
                    var totalPowerMeasure: Double? = 0.0
                    for (measure in sensorMeasureList) {
                        totalPowerMeasure = totalPowerMeasure!! + measure.power!!
                    }

                    val measure = EnergyUtil.getKwhInPeriod(
                            totalPowerMeasure,
                            sensorMeasureList.size,
                            intervalsFiveMinutes[indexIntervals].start.toDate(),
                            intervalsFiveMinutes[indexIntervals].end.toDate()
                    )

                    measuresList.add(measure!!)
                } else {
                    measuresList.add((0.0).toDouble())
                }

                if (intervalsFiveMinutes[indexIntervals].end.minuteOfHour < 10) {
                    fiveChartLabels.add(intervalsFiveMinutes[indexIntervals].end.hourOfDay.toString() + ":0" + intervalsFiveMinutes[indexIntervals].end.minuteOfHour + "H")
                } else {
                    fiveChartLabels.add(intervalsFiveMinutes[indexIntervals].end.hourOfDay.toString() + ":" + intervalsFiveMinutes[indexIntervals].end.minuteOfHour + "H")
                }

                indexIntervals++
            } while (indexIntervals < intervalsFiveMinutes.size)

            measuresList.reverse()

            var i = 0
            for (measure in measuresList) {
                entriesMeasures.add(Entry(i.toFloat(), measure.toFloat()))
                i++
            }

            return entriesMeasures
        }

    val xAxisChartLabels: List<String>
        get() {
            fiveChartLabels.reverse()
            return fiveChartLabels
        }
}
