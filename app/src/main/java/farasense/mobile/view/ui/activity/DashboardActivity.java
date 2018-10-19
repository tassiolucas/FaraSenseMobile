package farasense.mobile.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import butterknife.ButterKnife;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view.ui.adapter.ChartConsumeTabAdapter;
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
    private TabLayout tabLayout;
    private ChartConsumeTabAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(getString(R.string.welcome_label), null, false);

        dailyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(DailyChartConsumptionFragmentViewModel.class);
        monthlyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(MonthlyChartConsumptionFragmentViewModel.class);
        yearlyChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(YearlyChartConsumptionFragmentViewModel.class);

        tabLayout = (TabLayout) findViewById(R.id.consumption_chart_tabs);

        viewPagerAdapter = new ChartConsumeTabAdapter(getSupportFragmentManager(), 3);

        viewPager = (ViewPager) findViewById(R.id.chart_consumption_container);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(FIRST_TAB).setText(R.string.daily);
        tabLayout.getTabAt(SECOND_TAB).setText(R.string.monthly);
        tabLayout.getTabAt(THIRD_TAB).setText(R.string.yearly);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onResume() {
        super.onResume();
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
