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
import android.widget.ImageButton;
import android.widget.TextView;
import org.joda.time.DateTime;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import butterknife.ButterKnife;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.util.DateUtil;
import farasense.mobile.util.EnergyUtil;
import farasense.mobile.util.Preferences;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view.ui.activity.fragment.RealTimeCurrentIndicatorView;
import farasense.mobile.view.ui.adapter.ChartConsumeTabAdapter;
import farasense.mobile.view.ui.adapter.ChartLastConsumptionTabAdapter;
import farasense.mobile.view.ui.dialog.CostOptionDialog;
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

    public DashboardDataBinding binding;
    public DailyChartConsumptionFragmentViewModel dailyConsumptionFragmentViewModel;
    public MonthlyChartConsumptionFragmentViewModel monthlyConsumptionFragmentViewModel;
    public YearlyChartConsumptionFragmentViewModel yearlyChartConsumptionFragmentViewModel;
    public HourChartConsumptionFragmentViewModel hourChartConsumptionFragmentViewModel;
    public ThirtyChartConsumptionFragmentViewModel thirtyChartConsumptionFragmentViewModel;
    public FiveChartConsumptionFragmentViewModel fiveChartConsumptionFragmentViewModel;

    public RealTimeCurrentIndicatorView realTimeCurrentIndicatorView;

    private TabLayout tabLayoutConsumption;
    private TabLayout tabLayoutLastConsumption;
    private ChartConsumeTabAdapter viewPagerConsumptionAdapter;
    private ChartLastConsumptionTabAdapter viewPagerLastConsumptionAdapter;

    private ViewPager viewConsumptionPager;
    private ViewPager viewLastConsumptionPager;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextView startDateLabel;
    private TextView endDateLabel;
    private TextView aoLabel;
    private TextView costValue;

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
        thirtyChartConsumptionFragmentViewModel = ViewModelProviders.of(this).get(ThirtyChartConsumptionFragmentViewModel.class);
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

        realTimeCurrentIndicatorView = findViewById(R.id.real_time_current_indicator);

        final ImageButton button = findViewById(R.id.cost_button_option);
        button.setOnClickListener(v -> {
            final CostOptionDialog dialog = new CostOptionDialog(this);
            dialog.setContentView(R.layout.adapter_item_cost_option_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.show();
        });

        startDateLabel = findViewById(R.id.label_start_period_comsumption);
        endDateLabel = findViewById(R.id.label_end_period_comsumption);
        aoLabel = findViewById(R.id.label_ao);
        costValue = findViewById(R.id.label_cost_comsumption);
    }

    @Override
    public void onResume() {
        super.onResume();

        tabLayoutLastConsumption.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                fragmentManager.getFragments().get(tab.getPosition()).onPause();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Long maturityLong = Preferences.getInstance().getMaturityDate();
        if(maturityLong != null && maturityLong != 0) {
            startDateLabel.setVisibility(View.VISIBLE);
            endDateLabel.setVisibility(View.VISIBLE);
            aoLabel.setVisibility(View.VISIBLE);

            Timestamp maturityTimestamp = new Timestamp(maturityLong);
            DateTime maturityDate = new DateTime(maturityTimestamp);
            DateTime startDate = DateUtil.get30DaysAgo(maturityDate);
            DateTime endDate = maturityDate;

            startDateLabel.setText(startDate.getDayOfMonth() + "/" + startDate.getMonthOfYear());
            endDateLabel.setText(endDate.getDayOfMonth() + "/" + (endDate.getMonthOfYear()));

            Float rateKwh = Preferences.getInstance().getRateKwh();
            Float rateFlag = Preferences.getInstance().getRateFlag();

            Double totalCost = EnergyUtil.getValueCost(maturityDate, rateKwh, rateFlag);

            DecimalFormat format = new DecimalFormat("0.00");

            String formatted = format.format(totalCost);
            if (totalCost != null && totalCost != 0) {
                costValue.setText("RS " + formatted);
            }
        } else {
            startDateLabel.setVisibility(View.GONE);
            endDateLabel.setVisibility(View.GONE);
            aoLabel.setVisibility(View.GONE);
            costValue.setText("Insira suas opções");
            costValue.setTextSize(24);
        }
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onDestroy() {
        BaseObservableViewModel.stopServices(this);
        super.onDestroy();
    }
}
