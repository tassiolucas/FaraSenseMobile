package farasense.mobile.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import butterknife.ButterKnife;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view_model.AnnualChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.MonthlyChartConsumptionFragmentViewModel;
import farasense.mobile.view_model.base.BaseObservableViewModel;

public class DashboardActivity extends BaseActivity {

    private DashboardDataBinding binding;
    private DailyChartConsumptionFragmentViewModel dailyConsumptionFragmentViewModel;
    private MonthlyChartConsumptionFragmentViewModel monthlyConsumptionFragmentViewModel;
    private AnnualChartConsumptionFragmentViewModel annualChartConsumptionFragmentViewModel;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(getString(R.string.welcome_label), null, false);

        dailyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(DailyChartConsumptionFragmentViewModel.class);
        monthlyConsumptionFragmentViewModel = ViewModelProviders.of(this).get(MonthlyChartConsumptionFragmentViewModel.class);
        annualChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(AnnualChartConsumptionFragmentViewModel.class);

        tabLayout = (TabLayout) findViewById(R.id.consume_chart_tabs);

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
