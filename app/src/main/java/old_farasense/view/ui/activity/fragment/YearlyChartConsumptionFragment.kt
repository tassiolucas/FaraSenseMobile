//package old_farasense.view.ui.activity.fragment
//
//import android.arch.lifecycle.ViewModelProviders
//import android.databinding.DataBindingUtil
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.components.YAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.highlight.Highlight
//import farasense.mobile.R
//import farasense.mobile.databinding.YearlyChartConsumptionFragmentDataBinding
//import old_farasense.util.ChartUtil
//import old_farasense.view_model.YearlyChartConsumptionFragmentViewModel
//
//class YearlyChartConsumptionFragment : Fragment() {
//
//    private var binding: YearlyChartConsumptionFragmentDataBinding? = null
//    private var viewModel: YearlyChartConsumptionFragmentViewModel? = null
//    private var yearlyChart: BarChart? = null
//    private var entryList: List<BarEntry>? = null
//    private var dataSet: BarDataSet? = null
//    private var data: BarData? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yearly_chart_consumption, container, false)
//
//        viewModel = ViewModelProviders.of(activity!!).get(YearlyChartConsumptionFragmentViewModel::class.java)
//
//        val view = binding?.root
//
//        createYearlyChart()
//
//        binding?.yearlyCompumptionFragment = viewModel
//
//        return view
//    }
//
//    private fun createYearlyChart() {
//        yearlyChart = binding!!.yearlyChartConsumption
//        yearlyChart?.description?.text = resources.getString(R.string.description_yearly_chart)
//        yearlyChart?.isScaleYEnabled = false
//        yearlyChart?.setFitBars(true)
//        yearlyChart?.zoom(2.8f, 0f, 0f, 0f)
//
//        yearlyChart?.moveViewToX(20f)
//
//        configureValueSelectedListener(yearlyChart!!)
//
//        val xAxis = yearlyChart?.xAxis
//        val yAxis = yearlyChart?.axisLeft
//
//        entryList = viewModel!!.yearlyConsumption
//        xAxis?.valueFormatter = viewModel?.monthlyChartLabels?.let { ChartUtil.setChartLabels(it) }
//
//        xAxis?.let { yAxis?.let { it1 -> configureAxis(yearlyChart!!, it, it1) } }
//
//        dataSet = configureDataSet(entryList)
//
//        data = BarData(dataSet!!)
//
//        yearlyChart!!.data = data
//    }
//
//    private fun configureAxis(dailyChart: BarChart, xAxis: XAxis, yAxis: YAxis) {
//        dailyChart.animateY(3000)
//
//        xAxis.setDrawAxisLine(false)
//        xAxis.setDrawGridLines(false)
//        xAxis.isEnabled = true
//
//        dailyChart.axisRight.isEnabled = false
//        yAxis.setDrawLabels(true)
//        yAxis.setDrawAxisLine(false)
//        yAxis.setDrawGridLines(true)
//        yAxis.setDrawZeroLine(true)
//
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//    }
//
//    private fun configureDataSet(entryList: List<BarEntry>?): BarDataSet {
//        val dataSet: BarDataSet
//        dataSet = BarDataSet(entryList!!, resources.getString(R.string.kilowatts))
//        dataSet.color = resources.getColor(R.color.colorYearlyChartBar)
//        dataSet.setDrawValues(false)
//
//        return dataSet
//    }
//
//    private fun configureValueSelectedListener(yearlyChart: BarChart) {
//        yearlyChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
//            override fun onValueSelected(entry: Entry, h: Highlight) {
//                if (binding?.labelYearlyValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelYearlyValueSelected?.text = entry.y.toString()
//                } else {
//                    binding?.labelYearlyValueSelected?.visibility = View.VISIBLE
//                    binding?.labelYearlyValueSelected?.text = entry.y.toString()
//                }
//            }
//
//            override fun onNothingSelected() {
//                if (binding?.labelYearlyValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelYearlyValueSelected?.visibility = View.GONE
//                } else {
//                    binding?.labelYearlyValueSelected?.visibility = View.GONE
//                }
//            }
//        })
//    }
//}
