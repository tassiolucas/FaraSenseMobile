package farasense.mobile.view_model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.Entry
import java.util.ArrayList
import farasense.mobile.model.DAO.FaraSenseSensorDAO
import farasense.mobile.util.DateUtil
import farasense.mobile.util.EnergyUtil

class ThirtyChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var thirtyChartLabels: MutableList<String> = ArrayList()

    val thirtyMinutesPer12Hours: List<Entry>
        get() {
            var indexIntervals = 0
            val entriesMeasures = ArrayList<Entry>()
            val measuresList = ArrayList<Double>()
            val intervalsThirtyMinutes = DateUtil.allIntervalsLast12Hours
            thirtyChartLabels = ArrayList()

            do {
                val sensorMeasuresList = FaraSenseSensorDAO().getMeasureByIntervals(
                        intervalsThirtyMinutes[indexIntervals].start.toDate(),
                        intervalsThirtyMinutes[indexIntervals].end.toDate()
                )

                if (sensorMeasuresList != null) {
                    var totalPowerMeasure: Double? = 0.0
                    for (measure in sensorMeasuresList) {
                        totalPowerMeasure = totalPowerMeasure!! + measure.power!!
                    }

                    val measure = EnergyUtil.getKwhInPeriod(
                            totalPowerMeasure,
                            sensorMeasuresList.size,
                            intervalsThirtyMinutes[indexIntervals].start.toDate(),
                            intervalsThirtyMinutes[indexIntervals].end.toDate()
                    )

                    measure?.let { measuresList.add(it) }
                } else {
                    measuresList.add(java.lang.Double.valueOf(0.0))
                }

                if (intervalsThirtyMinutes[indexIntervals].start.minuteOfHour < 30) {
                    thirtyChartLabels.add(intervalsThirtyMinutes[indexIntervals].end.hourOfDay.toString() + ":30H")
                } else {
                    thirtyChartLabels.add(intervalsThirtyMinutes[indexIntervals].end.hourOfDay.toString() + "H")
                }

                indexIntervals++
            } while (indexIntervals < intervalsThirtyMinutes.size)

            measuresList.reverse()
            for ((i, measure) in measuresList.withIndex()) {
                entriesMeasures.add(Entry(i.toFloat(), measure.toFloat()))
            }

            return entriesMeasures
        }

    val xAxisChartLabels: List<String>
        get() {
            thirtyChartLabels.reverse()
            return thirtyChartLabels
        }
}
