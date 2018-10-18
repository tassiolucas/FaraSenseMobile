package farasense.mobile.view.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import farasense.mobile.view.ui.activity.fragment.AnnualChartConsumptionFragment;
import farasense.mobile.view.ui.activity.fragment.DailyChartConsumptionFragment;
import farasense.mobile.view.ui.activity.fragment.MonthlyChartConsumptionFragment;

public class ChartConsumeTabAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private DailyChartConsumptionFragment dailyChartConsumptionFragment;
    private MonthlyChartConsumptionFragment monthlyChartConsumptionFragment;
    private AnnualChartConsumptionFragment annualChartConsumptionFragment;
    private List<Fragment> fragmentList;

    public ChartConsumeTabAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;

        dailyChartConsumptionFragment = new DailyChartConsumptionFragment();
        monthlyChartConsumptionFragment = new MonthlyChartConsumptionFragment();
        annualChartConsumptionFragment = new AnnualChartConsumptionFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(dailyChartConsumptionFragment);
        fragmentList.add(monthlyChartConsumptionFragment);
        fragmentList.add(annualChartConsumptionFragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return dailyChartConsumptionFragment;
            case 1:
                return monthlyChartConsumptionFragment;
            case 2:
                return annualChartConsumptionFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
