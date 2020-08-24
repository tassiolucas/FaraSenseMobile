package old_farasense.view_model//package old_farasense.view_model
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import com.github.mikephil.charting.data.Entry
//import java.util.ArrayList
//import old_farasense.model.DAO.FaraSenseSensorDAO
//import old_farasense.util.DateUtil
//import old_farasense.util.EnergyUtil
//
//class ThirtyChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {
//
//    private var thirtyChartLabels: MutableList<String> = ArrayList()
//
//    val thirtyMinutesPer12Hours: List<Entry>
//        get() {
//            var indexIntervals = 0
//            val entriesMeasures = ArrayList<Entry>()
//            val measuresList = ArrayList<Double>()
//            val intervalsThirtyMinutes = DateUtil.allIntervalsLast12Hours
//            thirtyChartLabels = ArrayList()
//
//            do {
//                val sensorMeasuresList = FaraSenseSensorDAO().getMeasureByIntervals(
//                        intervalsThirtyMinutes[indexIntervals].start.toDate(),
//                        intervalsThirtyMinutes[indexIntervals].end.toDate()
//                )
//
//                if (sensorMeasuresList.isNotEmpty()) {
//                    var totalPowerMeasure: Double? = 0.0
//                    for (measure in sensorMeasuresList) {
//                        totalPowerMeasure = totalPowerMeasure!! + measure.power!!
//                    }
//
//                    val measure = EnergyUtil.getKwhInPeriod(
//                            totalPowerMeasure,
//                            sensorMeasuresList.size,
//                            intervalsThirtyMinutes[indexIntervals].start.toDate(),
//                            intervalsThirtyMinutes[indexIntervals].end.toDate()
//                    )
//
//                    measure?.let { measuresList.add(it) }
//                } else {
//                    measuresList.add((0.0).toDouble())
//                }
//
//                if (intervalsThirtyMinutes[indexIntervals].start.minuteOfHour < 30) {
//                    thirtyChartLabels.add(intervalsThirtyMinutes[indexIntervals].end.hourOfDay.toString() + ":30H")
//                } else {
//                    thirtyChartLabels.add(intervalsThirtyMinutes[indexIntervals].end.hourOfDay.toString() + "H")
//                }
//
//                indexIntervals++
//            } while (indexIntervals < intervalsThirtyMinutes.size)
//
//            measuresList.reverse()
//            for ((i, measure) in measuresList.withIndex()) {
//                entriesMeasures.add(Entry(i.toFloat(), measure.toFloat()))
//            }
//
//            return entriesMeasures
//        }
//
//    val xAxisChartLabels: List<String>
//        get() {
//            thirtyChartLabels.reverse()
//            return thirtyChartLabels
//        }
//}
