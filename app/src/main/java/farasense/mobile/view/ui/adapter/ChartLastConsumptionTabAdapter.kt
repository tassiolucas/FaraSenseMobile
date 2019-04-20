package farasense.mobile.view.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList
import farasense.mobile.view.ui.activity.fragment.FiveChartConsumptionFragment
import farasense.mobile.view.ui.activity.fragment.HourChartConsumptionFragment
import farasense.mobile.view.ui.activity.fragment.ThirtyChartConsumptionFragment

class ChartLastConsumptionTabAdapter(fragmentManager: FragmentManager, private val numOfTabs: Int) : FragmentPagerAdapter(fragmentManager) {
    private val hourChartConsumptionFragment: HourChartConsumptionFragment = HourChartConsumptionFragment()
    private val thirtyChartConsumptionFragment: ThirtyChartConsumptionFragment = ThirtyChartConsumptionFragment()
    private val fiveChartConsumptionFragment: FiveChartConsumptionFragment = FiveChartConsumptionFragment()
    private val fragmentsList: MutableList<Fragment>

    init {
        fragmentsList = ArrayList()
        fragmentsList.add(hourChartConsumptionFragment)
        fragmentsList.add(thirtyChartConsumptionFragment)
        fragmentsList.add(fiveChartConsumptionFragment)
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> hourChartConsumptionFragment
            1 -> thirtyChartConsumptionFragment
            2 -> fiveChartConsumptionFragment
            else -> null
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }
}
