package farasense.mobile.view.ui.activity.fragment

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import farasense.mobile.R
import farasense.mobile.databinding.DailyChartConsumptionFragmentDataBinding
import farasense.mobile.util.ChartUtil
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel

class DailyChartConsumptionFragment : Fragment() {

    private var binding: DailyChartConsumptionFragmentDataBinding? = null
    private var viewModel: DailyChartConsumptionFragmentViewModel? = null
    private var dailyChart: BarChart? = null
    private var entryList: List<BarEntry>? = null
    private var dataSet: BarDataSet? = null
    private var data: BarData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart_consumption, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(DailyChartConsumptionFragmentViewModel::class.java)

        val view = binding?.root

        createDailyChart()

        binding?.dailyCompumptionFragment = viewModel

        return view
    }

    override fun onStop() {
        super.onStop()
        dailyChart?.animateY(3000)
        dailyChart?.invalidate()
    }

    private fun createDailyChart() {
        dailyChart = binding?.dailyChartConsumption
        dailyChart?.description?.text = resources.getString(R.string.description_daily_chart)
        dailyChart?.isScaleYEnabled = false
        dailyChart?.setFitBars(true)
        dailyChart?.zoom(3.4f, 0f, 0f, 0f)

        dailyChart?.moveViewToX(20f)

        configureValueSelectedListener(dailyChart!!)

        val xAxis = dailyChart?.xAxis
        val yAxis = dailyChart?.axisLeft

        entryList = viewModel?.dailyConsumpition
        xAxis?.valueFormatter = viewModel?.dailyChartLabels?.let { ChartUtil.setChartLabels(it) }

        xAxis?.let { yAxis?.let { it1 -> configureAxis(dailyChart!!, it, it1) } }

        dataSet = configureDataSet(entryList)

        data = BarData(dataSet!!)

        dailyChart?.data = data
    }

    private fun configureAxis(dailyChart: BarChart, xAxis: XAxis, yAxis: YAxis) {
        dailyChart.animateY(3000)

        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.isEnabled = true

        dailyChart.axisRight.isEnabled = false
        yAxis.setDrawLabels(true)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(true)
        yAxis.setDrawZeroLine(true)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
    }

    private fun configureDataSet(entryList: List<BarEntry>?): BarDataSet {
        val dataSet: BarDataSet
        dataSet = BarDataSet(entryList!!, resources.getString(R.string.kilowatts))
        dataSet.color = resources.getColor(R.color.colorDailyChartBar)
        dataSet.setDrawValues(false)

        return dataSet
    }

    private fun configureValueSelectedListener(dailyChart: BarChart) {
        dailyChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, h: Highlight) {
                if (binding?.labelDailyValueSelected?.visibility == View.VISIBLE) {
                    binding?.labelDailyValueSelected?.text = entry.y.toString()
                } else {
                    binding?.labelDailyValueSelected?.visibility = View.VISIBLE
                    binding?.labelDailyValueSelected?.text = entry.y.toString()
                }
            }

            override fun onNothingSelected() {
                if (binding?.labelDailyValueSelected?.visibility == View.VISIBLE) {
                    binding?.labelDailyValueSelected?.visibility = View.GONE
                } else {
                    binding?.labelDailyValueSelected?.visibility = View.GONE
                }
            }
        })
    }
}


