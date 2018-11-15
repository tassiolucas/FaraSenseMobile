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
import farasense.mobile.databinding.YearlyChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.view_model.YearlyChartConsumptionFragmentViewModel;

public class YearlyChartConsumptionFragment extends Fragment {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private YearlyChartConsumptionFragmentDataBinding binding;
    private YearlyChartConsumptionFragmentViewModel viewModel;
    private BarChart yearlyChart;
    private List<BarEntry> entryList;
    private BarDataSet dataSet;
    private BarData data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yearly_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(YearlyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        createYearlyChart();

        binding.setYearlyCompumptionFragment(viewModel);

        return view;
    }

    private void createYearlyChart() {
        yearlyChart = binding.yearlyChartConsumption;
        yearlyChart.getDescription().setText(getResources().getString(R.string.description_yearly_chart));
        yearlyChart.setScaleYEnabled(false);
        yearlyChart.setFitBars(true);
        yearlyChart.zoom(2.8F, 0, 0,0);

        yearlyChart.moveViewToX(20);

        configureValueSelectedListener(yearlyChart);

        XAxis xAxis = yearlyChart.getXAxis();
        YAxis yAxis = yearlyChart.getAxisLeft();

        entryList = viewModel.getYearlyConsumption();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getMonthlyChartLabels()));

        configureAxis(yearlyChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList);

        data = new BarData(dataSet);

        yearlyChart.setData(data);
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
        dataSet.setColor(getResources().getColor(R.color.colorYearlyChartBar));
        dataSet.setDrawValues(false);

        return dataSet;
    }

    private void configureValueSelectedListener(BarChart yearlyChart) {
        yearlyChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                if (binding.labelYearlyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelYearlyValueSelected.setText(String.valueOf(entry.getY()));
                } else {
                    binding.labelYearlyValueSelected.setVisibility(View.VISIBLE);
                    binding.labelYearlyValueSelected.setText(String.valueOf(entry.getY()));
                }
            }

            @Override
            public void onNothingSelected() {
                if (binding.labelYearlyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelYearlyValueSelected.setVisibility(View.GONE);
                } else {
                    binding.labelYearlyValueSelected.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }
}
