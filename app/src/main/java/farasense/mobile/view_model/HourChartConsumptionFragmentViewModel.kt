package farasense.mobile.view_model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.Entry
import java.util.ArrayList
import farasense.mobile.model.DAO.FaraSenseSensorDAO
import farasense.mobile.util.DateUtil
import farasense.mobile.util.EnergyUtil

class HourChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var hourChartLabels: MutableList<String> = ArrayList()

    private var measure = 0.0

    // TODO: (OK!) - FAZER UM CÓDIGO QUE LEIA AS MEDIDAS RELATIVAS AO TEMPO EM 24 HORAS
    // TODO: -(OK!) INVERTER A PLOTAGEM DOS GRÁFICOS
    // TODO: - FAZER UM CODIGO COM LIVEDATA

    val hourPer24Hours: List<Entry>
        get() {
            var hoursBehind = 0
            val entriesMeasures = ArrayList<Entry>()
            val measuresList = ArrayList<Double>()
            val intervals24Hours = DateUtil.allIntervalsLast24Hours
            hourChartLabels = ArrayList()

            do {
                val sensorMeasuresList = FaraSenseSensorDAO().getMeasureByIntervals(
                        intervals24Hours[hoursBehind].start.toDate(),
                        intervals24Hours[hoursBehind].end.toDate()
                )

                if (sensorMeasuresList.isNotEmpty()) {
                    var totalPowerMeasure: Double? = 0.0
                    for (measure in sensorMeasuresList) {
                        totalPowerMeasure = totalPowerMeasure!! + measure.power!!
                    }

                    measure = if (!sensorMeasuresList.isEmpty()) {
                        EnergyUtil.getKwhInPeriod(
                                totalPowerMeasure,
                                sensorMeasuresList.size,
                                sensorMeasuresList[0].date,
                                sensorMeasuresList[sensorMeasuresList.size - 1].date
                        )!!
                    } else {
                        0.0
                    }

                    measuresList.add(measure!!)
                } else {
                    measuresList.add(java.lang.Double.valueOf(0.0))
                }

                hourChartLabels.add(intervals24Hours[hoursBehind].end.hourOfDay.toString() + "H")

                hoursBehind++
            } while (hoursBehind < intervals24Hours.size)

            measuresList.reverse()
            var i = 0
            for (measure in measuresList) {
                entriesMeasures.add(Entry(i.toFloat(), measure.toFloat()))
                i++
            }

            return entriesMeasures
        }

    fun getHourChartLabels(): List<String> {
        hourChartLabels.reverse()
        return hourChartLabels
    }
}
