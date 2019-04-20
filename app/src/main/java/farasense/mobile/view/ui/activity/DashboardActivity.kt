package farasense.mobile.view.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import org.joda.time.DateTime
import java.sql.Timestamp
import java.text.DecimalFormat
import butterknife.ButterKnife
import farasense.mobile.R
import farasense.mobile.databinding.DashboardDataBinding
import farasense.mobile.util.GpsUtil
import farasense.mobile.util.DateUtil
import farasense.mobile.util.EnergyUtil
import farasense.mobile.util.Preferences
import farasense.mobile.view.ui.activity.base.BaseActivity
import farasense.mobile.view.ui.activity.fragment.RealTimeCurrentIndicatorView
import farasense.mobile.view.ui.adapter.ChartConsumeTabAdapter
import farasense.mobile.view.ui.adapter.ChartLastConsumptionTabAdapter
import farasense.mobile.view.ui.dialog.CostOptionDialog
import farasense.mobile.view_model.FiveChartConsumptionFragmentViewModel
import farasense.mobile.view_model.HourChartConsumptionFragmentViewModel
import farasense.mobile.view_model.ThirtyChartConsumptionFragmentViewModel
import farasense.mobile.view_model.YearlyChartConsumptionFragmentViewModel
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel
import farasense.mobile.view_model.MonthlyChartConsumptionFragmentViewModel
import farasense.mobile.view_model.base.BaseObservableViewModel

class DashboardActivity : BaseActivity() {

    private val FIRST_TAB = 0
    private val SECOND_TAB = 1
    private val THIRD_TAB = 2

    lateinit var binding: DashboardDataBinding
    lateinit var dailyConsumptionFragmentViewModel: DailyChartConsumptionFragmentViewModel
    lateinit var monthlyConsumptionFragmentViewModel: MonthlyChartConsumptionFragmentViewModel
    lateinit var yearlyChartConsumptionFragmentViewModel: YearlyChartConsumptionFragmentViewModel
    lateinit var hourChartConsumptionFragmentViewModel: HourChartConsumptionFragmentViewModel
    lateinit var thirtyChartConsumptionFragmentViewModel: ThirtyChartConsumptionFragmentViewModel
    lateinit var fiveChartConsumptionFragmentViewModel: FiveChartConsumptionFragmentViewModel

    lateinit var realTimeCurrentIndicatorView: RealTimeCurrentIndicatorView

    private var tabLayoutConsumption: TabLayout? = null
    private var tabLayoutLastConsumption: TabLayout? = null
    private var viewPagerConsumptionAdapter: ChartConsumeTabAdapter? = null
    private var viewPagerLastConsumptionAdapter: ChartLastConsumptionTabAdapter? = null

    private var viewConsumptionPager: ViewPager? = null
    private var viewLastConsumptionPager: ViewPager? = null

    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null

    private var startDateLabel: TextView? = null
    private var endDateLabel: TextView? = null
    private var aoLabel: TextView? = null
    private var costValue: TextView? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        ButterKnife.bind(this)

        setToolbar(getString(R.string.welcome_label), null, false)

        dailyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(DailyChartConsumptionFragmentViewModel::class.java)
        monthlyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(MonthlyChartConsumptionFragmentViewModel::class.java)
        yearlyChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(YearlyChartConsumptionFragmentViewModel::class.java)

        hourChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(HourChartConsumptionFragmentViewModel::class.java)
        thirtyChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(ThirtyChartConsumptionFragmentViewModel::class.java)
        fiveChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(FiveChartConsumptionFragmentViewModel::class.java)

        tabLayoutConsumption = findViewById<View>(R.id.consumption_chart_tabs) as TabLayout
        tabLayoutLastConsumption = findViewById<View>(R.id.last_consumption_chart_tabs) as TabLayout

        viewPagerConsumptionAdapter = ChartConsumeTabAdapter(supportFragmentManager, 3)
        viewPagerLastConsumptionAdapter = ChartLastConsumptionTabAdapter(supportFragmentManager, 3)

        viewConsumptionPager = findViewById<View>(R.id.chart_consumption_container) as ViewPager
        viewLastConsumptionPager = findViewById<View>(R.id.last_chart_last_consumption_container) as ViewPager

        viewConsumptionPager?.adapter = viewPagerConsumptionAdapter
        viewLastConsumptionPager?.adapter = viewPagerLastConsumptionAdapter

        viewConsumptionPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutConsumption))
        viewLastConsumptionPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutLastConsumption))

        tabLayoutConsumption?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewConsumptionPager))
        tabLayoutLastConsumption?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewLastConsumptionPager))

        tabLayoutConsumption?.setupWithViewPager(viewConsumptionPager)
        tabLayoutLastConsumption?.setupWithViewPager(viewLastConsumptionPager)

        tabLayoutConsumption?.getTabAt(FIRST_TAB)?.setText(R.string.daily)
        tabLayoutConsumption?.getTabAt(SECOND_TAB)?.setText(R.string.monthly)
        tabLayoutConsumption?.getTabAt(THIRD_TAB)?.setText(R.string.yearly)

        tabLayoutLastConsumption?.getTabAt(FIRST_TAB)?.setText(R.string.hour)
        tabLayoutLastConsumption?.getTabAt(SECOND_TAB)?.setText(R.string.thirty_minutes)
        tabLayoutLastConsumption?.getTabAt(THIRD_TAB)?.setText(R.string.five_minutes)

        fragmentManager = supportFragmentManager
        fragmentManager?.executePendingTransactions()

        fragmentTransaction = fragmentManager?.beginTransaction()

        realTimeCurrentIndicatorView = findViewById(R.id.real_time_current_indicator)

        val button = findViewById<ImageButton>(R.id.cost_button_option)
        button.setOnClickListener {
            val dialog = CostOptionDialog(this)
            dialog.setContentView(R.layout.adapter_item_cost_option_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)
            dialog.show()
        }

        startDateLabel = findViewById(R.id.label_start_period_comsumption)
        endDateLabel = findViewById(R.id.label_end_period_comsumption)
        aoLabel = findViewById(R.id.label_ao)
        costValue = findViewById(R.id.label_cost_comsumption)
    }

    @SuppressLint("SetTextI18n")
    public override fun onResume() {
        super.onResume()

        if (!GpsUtil.isLocationEnabled(applicationContext))
            GpsUtil.alertGgpDialog(this)

        tabLayoutLastConsumption?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {
                fragmentManager!!.fragments[tab.position].onPause()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val maturityLong = Preferences.getInstance().maturityDate
        if (maturityLong != null && maturityLong != 0L) {
            startDateLabel?.visibility = View.VISIBLE
            endDateLabel?.visibility = View.VISIBLE
            aoLabel?.visibility = View.VISIBLE

            val maturityTimestamp = Timestamp(maturityLong)
            val maturityDate = DateTime(maturityTimestamp)
            val startDate = DateUtil.get30DaysAgo(maturityDate)

            startDateLabel?.text = startDate.dayOfMonth.toString() + "/" + startDate.monthOfYear
            endDateLabel?.text = maturityDate.dayOfMonth.toString() + "/" + maturityDate.monthOfYear

            val rateKwh = Preferences.getInstance().rateKwh
            val rateFlag = Preferences.getInstance().rateFlag

            val totalCost = EnergyUtil.getValueCost(maturityDate, rateKwh, rateFlag)

            val format = DecimalFormat("0.00")

            val formatted = format.format(totalCost)
            if (totalCost != null && totalCost != 0.0) {
                costValue?.text = "RS $formatted"
            }
        } else {
            startDateLabel?.visibility = View.GONE
            endDateLabel?.visibility = View.GONE
            aoLabel?.visibility = View.GONE
            costValue?.text = "Insira suas opções"
            costValue?.textSize = 24f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    public override fun onDestroy() {
        BaseObservableViewModel.stopServices(this)
        super.onDestroy()
    }
}