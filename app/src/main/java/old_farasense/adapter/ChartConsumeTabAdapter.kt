//package old_farasense.adapter
//
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.support.v4.app.FragmentPagerAdapter
//import java.util.ArrayList
//import old_farasense.view.ui.activity.fragment.YearlyChartConsumptionFragment
//import old_farasense.view.ui.activity.fragment.DailyChartConsumptionFragment
//import old_farasense.view.ui.activity.fragment.MonthlyChartConsumptionFragment
//
//class ChartConsumeTabAdapter(fragmentManager: FragmentManager, private val numOfTabs: Int) : FragmentPagerAdapter(fragmentManager) {
//    private val dailyChartConsumptionFragment: DailyChartConsumptionFragment = DailyChartConsumptionFragment()
//    private val monthlyChartConsumptionFragment: MonthlyChartConsumptionFragment = MonthlyChartConsumptionFragment()
//    private val yearlyChartConsumptionFragment: YearlyChartConsumptionFragment = YearlyChartConsumptionFragment()
//    private val fragmentList: MutableList<Fragment>
//
//    init {
//        fragmentList = ArrayList()
//        fragmentList.add(dailyChartConsumptionFragment)
//        fragmentList.add(monthlyChartConsumptionFragment)
//        fragmentList.add(yearlyChartConsumptionFragment)
//    }
//
//    override fun getItem(position: Int): Fragment? {
//        return when (position) {
//            0 -> dailyChartConsumptionFragment
//            1 -> monthlyChartConsumptionFragment
//            2 -> yearlyChartConsumptionFragment
//
//            else -> null
//        }
//    }
//
//    override fun getCount(): Int {
//        return numOfTabs
//    }
//}
