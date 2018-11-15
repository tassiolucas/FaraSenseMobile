package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
import javax.annotation.Nullable;
import farasense.mobile.R;
import farasense.mobile.databinding.DailyChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel;

public class DailyChartConsumptionFragment extends Fragment {

    private DailyChartConsumptionFragmentDataBinding binding;
    private DailyChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private BarChart dailyChart;
    private List<BarEntry> entryList;
    private BarDataSet dataSet;
    private BarData data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(DailyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        createDailyChart();

        binding.setDailyCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        dailyChart.animateY( 3000);
        dailyChart.invalidate();
    }

    private void createDailyChart() {
        dailyChart = binding.dailyChartConsumption;
        dailyChart.getDescription().setText(getResources().getString(R.string.description_daily_chart));
        dailyChart.setScaleYEnabled(false);
        dailyChart.setFitBars(true);
        dailyChart.zoom(3.4F, 0, 0,0);

        dailyChart.moveViewToX(20);

        configureValueSelectedListener(dailyChart);

        XAxis xAxis = dailyChart.getXAxis();
        YAxis yAxis = dailyChart.getAxisLeft();

        entryList = viewModel.getDailyConsumpition();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getDailyChartLabels()));

        configureAxis(dailyChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList);

        data = new BarData(dataSet);

        dailyChart.setData(data);
    }

    private void configureAxis(BarChart dailyChart, XAxis xAxis, YAxis yAxis) {
        dailyChart.animateY(3000);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);

        dailyChart.getAxisRight().setEnabled(false);
        yAxis.setDrawLabels(true);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawZeroLine(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1F);
    }

    private BarDataSet configureDataSet(List<BarEntry> entryList) {
        BarDataSet dataSet;
        dataSet = new BarDataSet(entryList, getResources().getString(R.string.kilowatts));
        dataSet.setColor(getResources().getColor(R.color.colorDailyChartBar));
        dataSet.setDrawValues(false);

        return dataSet;
    }

    private void configureValueSelectedListener(BarChart dailyChart) {
        dailyChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                if (binding.labelDailyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelDailyValueSelected.setText(String.valueOf(entry.getY()));
                } else {
                    binding.labelDailyValueSelected.setVisibility(View.VISIBLE);
                    binding.labelDailyValueSelected.setText(String.valueOf(entry.getY()));
                }
            }

            @Override
            public void onNothingSelected() {
                if (binding.labelDailyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelDailyValueSelected.setVisibility(View.GONE);
                } else {
                    binding.labelDailyValueSelected.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}


