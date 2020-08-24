//package old_farasense.view.ui.activity.fragment
//
//import android.arch.lifecycle.ViewModelProviders
//import android.databinding.DataBindingUtil
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v4.content.ContextCompat
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.github.mikephil.charting.charts.LineChart
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.components.YAxis
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import com.github.mikephil.charting.highlight.Highlight
//import farasense.mobile.R
//import farasense.mobile.databinding.HourChartConsumptionFragmentDataBinding
//import old_farasense.util.ChartUtil
//import old_farasense.view_model.HourChartConsumptionFragmentViewModel
//
//class HourChartConsumptionFragment : Fragment() {
//
//    private var binding: HourChartConsumptionFragmentDataBinding? = null
//    private var viewModel: HourChartConsumptionFragmentViewModel? = null
//    private var hourChart: LineChart? = null
//    private var entryList: List<Entry>? = null
//    private var dataSet: LineDataSet? = null
//    private var data: LineData? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hour_chart_consumption, container, false)
//
//        viewModel = ViewModelProviders.of(activity!!).get(HourChartConsumptionFragmentViewModel::class.java)
//
//        val view = binding?.root
//
//        createHourChart()
//
//        binding?.hourCompumptionFragment = viewModel
//
//        return view
//    }
//
//    override fun onStop() {
//        super.onStop()
//        hourChart?.animateY(3000)
//        hourChart?.invalidate()
//    }
//
//    private fun createHourChart() {
//        hourChart = binding?.hourChartConsumption
//        hourChart?.description?.text = resources.getString(R.string.description_hour_chart)
//        hourChart?.isScaleYEnabled = false
//
//        configureValueSelectedListener(hourChart!!)
//
//        val xAxis = hourChart?.xAxis
//        val yAxis = hourChart?.axisLeft
//
//        // Popula as entradas e os labels
//        entryList = viewModel!!.hourPer24Hours
//        xAxis?.valueFormatter = ChartUtil.setChartLabels(viewModel!!.getHourChartLabels())
//
//        // Configura os eixos
//        xAxis?.let { yAxis?.let { it1 -> configureAxis(hourChart!!, it, it1) } }
//
//        dataSet = configureDataSet(entryList) // Prepara os dados para gráfico
//
//        data = LineData(dataSet!!)
//
//        hourChart?.data = data // Posta os dados no gráfico
//    }
//
//    private fun configureAxis(hourChart: LineChart, xAxis: XAxis, yAxis: YAxis) {
//        hourChart.animateY(3000)
//
//        xAxis.setDrawAxisLine(false)
//        xAxis.setDrawGridLines(true)
//        xAxis.isEnabled = true
//
//        hourChart.axisRight.isEnabled = false
//        yAxis.setDrawLabels(true)
//        yAxis.setDrawAxisLine(false)
//        yAxis.setDrawGridLines(false)
//        yAxis.setDrawZeroLine(true)
//
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//    }
//
//    private fun configureDataSet(entryList: List<Entry>?): LineDataSet {
//        val dataSet = LineDataSet(entryList, resources.getString(R.string.kilowatts))
//        dataSet.color = R.color.colorHourChartLine
//        dataSet.setCircleColor(R.color.colorHourChartPoint)
//        dataSet.setDrawValues(false)
//
//        dataSet.setDrawFilled(true)
//
//        val drawable = ContextCompat.getDrawable(context!!, R.drawable.fade_hour_chart)
//
//        dataSet.fillDrawable = drawable
//        return dataSet
//    }
//
//    private fun configureValueSelectedListener(hourChart: LineChart) {
//        hourChart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
//            override fun onValueSelected(entry: Entry, h: Highlight) {
//                if (binding?.labelHourValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelHourValueSelected?.text = entry.y.toString()
//                } else {
//                    binding?.labelHourValueSelected?.visibility = View.VISIBLE
//                    binding?.labelHourValueSelected?.text = entry.y.toString()
//                }
//            }
//
//            override fun onNothingSelected() {
//                if (binding?.labelHourValueSelected?.visibility == View.VISIBLE) {
//                    binding?.labelHourValueSelected?.visibility = View.GONE
//                } else {
//                    binding?.labelHourValueSelected?.visibility = View.GONE
//                }
//            }
//        })
//    }
//}
