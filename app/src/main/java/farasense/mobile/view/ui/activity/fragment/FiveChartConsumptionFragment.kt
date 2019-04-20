package farasense.mobile.view.ui.activity.fragment

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import farasense.mobile.R
import farasense.mobile.databinding.FiveChartConsumptionFragmentDataBinding
import farasense.mobile.util.ChartUtil
import farasense.mobile.view_model.FiveChartConsumptionFragmentViewModel

class FiveChartConsumptionFragment : Fragment() {

    private lateinit var binding: FiveChartConsumptionFragmentDataBinding
    private lateinit var viewModel: FiveChartConsumptionFragmentViewModel
    private lateinit var fiveChart: LineChart
    private lateinit var entryList: List<Entry>
    private lateinit var dataSet: LineDataSet
    private lateinit var data: LineData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_five_chart_consumption, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(FiveChartConsumptionFragmentViewModel::class.java)

        val view = binding.root

        createFiveChart()

        binding.fiveCompumptionFragment = viewModel

        return view
    }

    override fun onStop() {
        super.onStop()
        fiveChart.animateY(3000)
        fiveChart.invalidate()
    }

    private fun createFiveChart() {
        fiveChart = binding.fiveChartConsumption
        fiveChart.description.text = resources.getString(R.string.description_five_chart)
        fiveChart.isScaleYEnabled = false

        configureValueSelectedListener(fiveChart)

        val xAxis = fiveChart.xAxis
        val yAxis = fiveChart.axisLeft

        // Popula as entradas e os labels
        entryList = viewModel.hourPerFiveMinutes
        xAxis.valueFormatter = ChartUtil.setChartLabels(viewModel.xAxisChartLabels)

        // Configura os eixos
        configureAxis(fiveChart, xAxis, yAxis)

        dataSet = configureDataSet(entryList) // Prepara os dados para gráfico

        data = LineData(dataSet)

        fiveChart.data = data // Posta os dados no gráfico
    }

    private fun configureAxis(hourChart: LineChart, xAxis: XAxis, yAxis: YAxis) {
        hourChart.animateY(3000)

        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.isEnabled = true

        hourChart.axisRight.isEnabled = false
        yAxis.setDrawLabels(true)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(false)
        yAxis.setDrawZeroLine(true)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
    }

    private fun configureDataSet(entryList: List<Entry>?): LineDataSet {
        val dataSet = LineDataSet(entryList, resources.getString(R.string.kilowatts))
        dataSet.color = R.color.colorFiveChartLine
        dataSet.setCircleColor(R.color.colorFiveChartPoint)
        dataSet.setDrawValues(false)

        dataSet.setDrawFilled(true)

        val drawable = ContextCompat.getDrawable(context!!, R.drawable.fade_five_chart)

        dataSet.fillDrawable = drawable
        return dataSet
    }

    private fun configureValueSelectedListener(hourChart: LineChart) {
        hourChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, h: Highlight) {
                if (binding.labelFiveValueSelected.visibility == View.VISIBLE) {
                    binding.labelFiveValueSelected.text = entry.y.toString()
                } else {
                    binding.labelFiveValueSelected.visibility = View.VISIBLE
                    binding.labelFiveValueSelected.text = entry.y.toString()
                }
            }

            override fun onNothingSelected() {
                if (binding.labelFiveValueSelected.visibility == View.VISIBLE) {
                    binding.labelFiveValueSelected.visibility = View.GONE
                } else {
                    binding.labelFiveValueSelected.visibility = View.GONE
                }
            }
        })
    }
}
