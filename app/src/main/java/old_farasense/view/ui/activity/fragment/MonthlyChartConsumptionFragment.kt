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
//import farasense.mobile.databinding.MonthlyChartConsumptionFragmentDataBinding
//import old_farasense.util.ChartUtil
//import old_farasense.view_model.MonthlyChartConsumptionFragmentViewModel
//
//class MonthlyChartConsumptionFragment : Fragment() {
//
//    private var binding: MonthlyChartConsumptionFragmentDataBinding? = null
//    private var viewModel: MonthlyChartConsumptionFragmentViewModel? = null
//    private var monthlyChart: BarChart? = null
//    private var entryList: List<BarEntry>? = null
//    private var dataSet: BarDataSet? = null
//    private var data: BarData? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_chart_consumption, container, false)
//
//        viewModel = ViewModelProviders.of(activity!!).get(MonthlyChartConsumptionFragmentViewModel::class.java)
//
//        val view = binding?.root
//
//        createMonthlyChart()
//
//        binding?.monthlyCompumptionFragment = viewModel
//
//        return view
//    }
//
//    private fun createMonthlyChart() {
//        monthlyChart = binding?.monthlyChartConsumption
//        monthlyChart?.description?.text = resources.getString(R.string.description_monthly_chart)
//        monthlyChart?.isScaleYEnabled = false
//        monthlyChart?.setFitBars(true)
//        monthlyChart?.zoom(3.0f, 0f, 0f, 0f)
//
//        monthlyChart?.moveViewToX(20f)
//
//        configureValueSelectedListener(monthlyChart!!)
//
//        val xAxis = monthlyChart?.xAxis
//        val yAxis = monthlyChart?.axisLeft
//
//        entryList = viewModel?.monthlyConsumption
//        xAxis?.valueFormatter = viewModel?.getMonthlyChartLabels()?.let { ChartUtil.setChartLabels(it) }
//
//        xAxis?.let { yAxis?.let { it1 -> configureAxis(monthlyChart!!, it, it1) } }
//
//        dataSet = configureDataSet(entryList)
//
//        data = BarData(dataSet!!)
//
//        monthlyChart?.data = data
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
//        dataSet.color = resources.getColor(R.color.colorMonthlyChartBar)
//        dataSet.setDrawValues(false)
//
//        return dataSet
//    }
//
//    private fun configureValueSelectedListener(monthlyChart: BarChart) {
//        monthlyChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
//            override fun onValueSelected(entry: Entry, h: Highlight) {
//                if (binding?.labelMonthlyValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelMonthlyValueSelected?.text = entry.y.toString()
//                } else {
//                    binding?.labelMonthlyValueSelected?.visibility = View.VISIBLE
//                    binding?.labelMonthlyValueSelected?.text = entry.y.toString()
//                }
//            }
//
//            override fun onNothingSelected() {
//                if (binding?.labelMonthlyValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelMonthlyValueSelected?.visibility = View.GONE
//                } else {
//                    binding?.labelMonthlyValueSelected?.visibility = View.GONE
//                }
//            }
//        })
//    }
//}
