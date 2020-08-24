package old_farasense.view_model//package old_farasense.view_model
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import com.github.mikephil.charting.data.BarEntry
//import old_farasense.model.DAO.FaraSenseSensorDailyDAO
//import old_farasense.util.DateUtil
//import java.util.*
//
//class YearlyChartConsumptionFragmentViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val yearlyChartLabels = ArrayList<String>()
//
//    val yearlyConsumption: List<BarEntry>
//        get() {
//            var yearlyBehind = 0
//            val entriesMeasures = ArrayList<BarEntry>()
//            val measureList = ArrayList<Double>()
//            val intervals10Year = DateUtil.allIntervalsLast10Year
//
//            do {
//                val sensorMeasuresList = FaraSenseSensorDailyDAO().getMeasureByIntervals(
//                        intervals10Year[yearlyBehind].start.toDate(),
//                        intervals10Year[yearlyBehind].end.toDate()
//                )
//
//                if (sensorMeasuresList != null) {
//                    var totalKhm: Double? = 0.0
//                    for (measure in sensorMeasuresList) {
//                        totalKhm = totalKhm!! + measure.totalKwh!!
//                    }
//
//                    totalKhm?.let { measureList.add(it) }
//                } else {
//                    measureList.add(java.lang.Double.valueOf(0.0))
//                }
//
//                yearlyChartLabels.add(intervals10Year[yearlyBehind].start.year.toString())
//
//                yearlyBehind++
//            } while (yearlyBehind < intervals10Year.size)
//
//            measureList.reverse()
//
//            var i = 0
//            for (measure in measureList) {
//                entriesMeasures.add(i, BarEntry(
//                        java.lang.Float.valueOf(i.toFloat()),
//                        java.lang.Float.valueOf(measure.toFloat())
//                ))
//                i++
//            }
//
//            return entriesMeasures
//        }
//
//    val monthlyChartLabels: List<String>
//        get() {
//            yearlyChartLabels.reverse()
//            return yearlyChartLabels
//        }
//}
