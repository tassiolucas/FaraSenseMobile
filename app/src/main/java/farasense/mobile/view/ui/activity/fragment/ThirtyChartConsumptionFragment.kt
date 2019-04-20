package farasense.mobile.view.ui.activity.fragment

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
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
import farasense.mobile.databinding.ThirtyChartConsumptionFragmentDataBinding
import farasense.mobile.util.ChartUtil
import farasense.mobile.view_model.ThirtyChartConsumptionFragmentViewModel

class ThirtyChartConsumptionFragment : Fragment() {

    private var binding: ThirtyChartConsumptionFragmentDataBinding? = null
    private var viewModel: ThirtyChartConsumptionFragmentViewModel? = null
    private var thirtyChart: LineChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_thirty_chart_consumption, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(ThirtyChartConsumptionFragmentViewModel::class.java)

        val view = binding?.root

        createThirtyChart()

        binding?.thirtyCompumptionFragment = viewModel

        return view
    }

    override fun onStop() {
        super.onStop()
        thirtyChart?.animateY(3000)
        thirtyChart?.invalidate()
    }

    private fun createThirtyChart() {
        thirtyChart = binding?.thirtyChartConsumption
        thirtyChart?.description?.text = resources.getString(R.string.description_thirty_chart)
        thirtyChart?.isScaleYEnabled = false

        configureValueSelectedListener(thirtyChart!!)

        val xAxis = thirtyChart?.xAxis
        val yAxis = thirtyChart?.axisLeft
        val dataSet: LineDataSet
        val data: LineData

        // Popula as entradas e os labels
        val entryList = viewModel?.thirtyMinutesPer12Hours
        xAxis?.valueFormatter = viewModel?.xAxisChartLabels?.let { ChartUtil.setChartLabels(it) }

        xAxis?.let { yAxis?.let { it1 -> configureAxis(thirtyChart!!, it, it1) } }

        dataSet = configureDataSet(entryList!!)

        data = LineData(dataSet)

        thirtyChart?.data = data
    }

    private fun configureAxis(thirtyChart: LineChart, xAxis: XAxis, yAxis: YAxis) {
        thirtyChart.animateY(3000)

        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.isEnabled = true

        thirtyChart.axisRight.isEnabled = false
        yAxis.setDrawLabels(true)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(false)
        yAxis.setDrawZeroLine(true)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
    }

    private fun configureDataSet(entryList: List<Entry>): LineDataSet {
        val dataSet = LineDataSet(entryList, resources.getString(R.string.kilowatts))
        dataSet.color = R.color.colorThirtyChartLine
        dataSet.setCircleColor(R.color.colorThirtyChartPoint)
        dataSet.setDrawValues(false)

        dataSet.setDrawFilled(true)

        val drawable = ContextCompat.getDrawable(context!!, R.drawable.fade_thirty_chart)

        dataSet.fillDrawable = drawable
        return dataSet
    }

    private fun configureValueSelectedListener(thirtyChart: LineChart) {
        thirtyChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, h: Highlight) {
                if (binding?.labelThirtyValueSelected?.visibility == View.VISIBLE) {
                    binding?.labelThirtyValueSelected?.text = entry.y.toString()
                } else {
                    binding?.labelThirtyValueSelected?.visibility = View.VISIBLE
                    binding?.labelThirtyValueSelected?.text = entry.y.toString()
                }
            }

            override fun onNothingSelected() {
                if (binding?.labelThirtyValueSelected?.visibility == View.VISIBLE) {
                    binding?.labelThirtyValueSelected?.visibility = View.GONE
                } else {
                    binding?.labelThirtyValueSelected?.visibility = View.GONE
                }
            }
        })
    }
}
