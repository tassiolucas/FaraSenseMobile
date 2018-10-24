package farasense.mobile.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

import butterknife.ButterKnife;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view.ui.adapter.ChartConsumeTabAdapter;
import farasense.mobile.view.ui.adapter.ChartLastConsumptionTabAdapter;
import farasense.mobile.view_model.FiveChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.HourChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.ThirtyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.YearlyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.MonthlyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.base.BaseObservableViewModel;

public class DashboardActivity extends BaseActivity {

    private final int FIRST_TAB = 0;
    private final int SECOND_TAB = 1;
    private final int THIRD_TAB = 2;

    private DashboardDataBinding binding;
    private DailyChartConsumptionFragmentViewModel dailyConsumptionFragmentViewModel;
    private MonthlyChartConsumptionFragmentViewModel monthlyConsumptionFragmentViewModel;
    private YearlyChartConsumptionFragmentViewModel yearlyChartConsumptionFragmentViewModel;
    private HourChartConsumptionFragmentViewModel hourChartConsumptionFragmentViewModel;
    private ThirtyChartConsumptionFragmentViewModel thirtyChartConsumptionFragmentViewModel;
    private FiveChartConsumptionFragmentViewModel fiveChartConsumptionFragmentViewModel;

    private TabLayout tabLayoutConsumption;
    private TabLayout tabLayoutLastConsumption;
    private ChartConsumeTabAdapter viewPagerConsumptionAdapter;
    private ChartLastConsumptionTabAdapter viewPagerLastConsumptionAdapter;

    private ViewPager viewConsumptionPager;
    private ViewPager viewLastConsumptionPager;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(getString(R.string.welcome_label), null, false);

        dailyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(DailyChartConsumptionFragmentViewModel.class);
        monthlyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(MonthlyChartConsumptionFragmentViewModel.class);
        yearlyChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(YearlyChartConsumptionFragmentViewModel.class);

        hourChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(HourChartConsumptionFragmentViewModel.class);
        monthlyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(MonthlyChartConsumptionFragmentViewModel.class);
        fiveChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(FiveChartConsumptionFragmentViewModel.class);

        tabLayoutConsumption = (TabLayout) findViewById(R.id.consumption_chart_tabs);
        tabLayoutLastConsumption = (TabLayout) findViewById(R.id.last_consumption_chart_tabs);

        viewPagerConsumptionAdapter = new ChartConsumeTabAdapter(getSupportFragmentManager(), 3);
        viewPagerLastConsumptionAdapter = new ChartLastConsumptionTabAdapter(getSupportFragmentManager(), 3);

        viewConsumptionPager = (ViewPager) findViewById(R.id.chart_consumption_container);
        viewLastConsumptionPager = (ViewPager) findViewById(R.id.last_chart_last_consumption_container);

        viewConsumptionPager.setAdapter(viewPagerConsumptionAdapter);
        viewLastConsumptionPager.setAdapter(viewPagerLastConsumptionAdapter);

        viewConsumptionPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutConsumption));
        viewLastConsumptionPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutLastConsumption));

        tabLayoutConsumption.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewConsumptionPager));
        tabLayoutLastConsumption.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewLastConsumptionPager));

        tabLayoutConsumption.setupWithViewPager(viewConsumptionPager);
        tabLayoutLastConsumption.setupWithViewPager(viewLastConsumptionPager);

        tabLayoutConsumption.getTabAt(FIRST_TAB).setText(R.string.daily);
        tabLayoutConsumption.getTabAt(SECOND_TAB).setText(R.string.monthly);
        tabLayoutConsumption.getTabAt(THIRD_TAB).setText(R.string.yearly);

        tabLayoutLastConsumption.getTabAt(FIRST_TAB).setText(R.string.hour);
        tabLayoutLastConsumption.getTabAt(SECOND_TAB).setText(R.string.thirty_minutes);
        tabLayoutLastConsumption.getTabAt(THIRD_TAB).setText(R.string.five_minutes);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();

        fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();

        tabLayoutLastConsumption.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentManager.getFragments().get(tab.getPosition()).onResume();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseObservableViewModel.stopServices(this);
    }

}
