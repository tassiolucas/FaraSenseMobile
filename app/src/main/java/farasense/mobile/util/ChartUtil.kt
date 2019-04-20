package farasense.mobile.util

import com.github.mikephil.charting.formatter.IAxisValueFormatter

object ChartUtil {

    fun setChartLabels(chartLabels: List<String>): IAxisValueFormatter {
        return IAxisValueFormatter { value, axis -> chartLabels[value.toInt()] }
    }
}
