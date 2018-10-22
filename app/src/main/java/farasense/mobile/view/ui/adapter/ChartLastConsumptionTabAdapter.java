package farasense.mobile.view.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import farasense.mobile.view.ui.activity.fragment.FiveChartConsumptionFragment;
import farasense.mobile.view.ui.activity.fragment.HourChartConsumptionFragment;
import farasense.mobile.view.ui.activity.fragment.ThirtyChartConsumptionFragment;

public class ChartLastConsumptionTabAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private HourChartConsumptionFragment hourChartConsumptionFragment;
    private ThirtyChartConsumptionFragment thirtyChartConsumptionFragment;
    private FiveChartConsumptionFragment fiveChartConsumptionFragment;
    private List<Fragment> fragmentsList;

    public ChartLastConsumptionTabAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;

        hourChartConsumptionFragment = new HourChartConsumptionFragment();
        thirtyChartConsumptionFragment = new ThirtyChartConsumptionFragment();
        fiveChartConsumptionFragment = new FiveChartConsumptionFragment();

        fragmentsList = new ArrayList<>();
        fragmentsList.add(hourChartConsumptionFragment);
        fragmentsList.add(thirtyChartConsumptionFragment);
        fragmentsList.add(fiveChartConsumptionFragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return hourChartConsumptionFragment;
            case 1:
                return thirtyChartConsumptionFragment;
            case 2:
                return fiveChartConsumptionFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() { return numOfTabs; }

}
