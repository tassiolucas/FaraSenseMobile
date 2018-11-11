package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

import javax.annotation.Nullable;
import farasense.mobile.R;
import farasense.mobile.databinding.ThirtyChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.view_model.ThirtyChartConsumptionFragmentViewModel;

public class ThirtyChartConsumptionFragment extends Fragment {

    private ThirtyChartConsumptionFragmentDataBinding binding;
    private ThirtyChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private LineChart thirtyChart;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_thirty_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(ThirtyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        createThirtyChart();

        binding.setThirtyCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        thirtyChart.animateY( 3000);
        thirtyChart.invalidate();
    }

    private void createThirtyChart() {
        thirtyChart = binding.thirtyChartConsumption;
        thirtyChart.getDescription().setText(getResources().getString(R.string.description_thirty_chart));
        thirtyChart.setScaleYEnabled(false);

        configureValueSelectedListener(thirtyChart);

        XAxis xAxis = thirtyChart.getXAxis();
        YAxis yAxis = thirtyChart.getAxisLeft();
        LineDataSet dataSet;
        LineData data;

        // Popula as entradas e os labels
        List<Entry> entryList = viewModel.getThirtyMinutesPer12Hours();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getXAxisChartLabels()));

        configureAxis(thirtyChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList);

        data = new LineData(dataSet);

        thirtyChart.setData(data);
    }

    private void configureAxis(LineChart thirtyChart, XAxis xAxis, YAxis yAxis) {
        thirtyChart.animateY(3000);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setEnabled(true);

        thirtyChart.getAxisRight().setEnabled(false);
        yAxis.setDrawLabels(true);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawZeroLine(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1F);
    }

    @NonNull
    private LineDataSet configureDataSet(List<Entry> entryList) {
        LineDataSet dataSet;
        dataSet = new LineDataSet(entryList, getResources().getString(R.string.kilowatts));
        dataSet.setColor(R.color.colorThirtyChartLine);
        dataSet.setCircleColor(R.color.colorThirtyChartPoint);
        dataSet.setDrawValues(false);

        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_thirty_chart);

        dataSet.setFillDrawable(drawable);
        return dataSet;
    }

    private void configureValueSelectedListener(LineChart hourChart) {
        hourChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                if (binding.labelThirtyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelThirtyValueSelected.setText(String.valueOf(entry.getY()));
                } else {
                    binding.labelThirtyValueSelected.setVisibility(View.VISIBLE);
                    binding.labelThirtyValueSelected.setText(String.valueOf(entry.getY()));
                }
            }

            @Override
            public void onNothingSelected() {
                if (binding.labelThirtyValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelThirtyValueSelected.setVisibility(View.GONE);
                } else {
                    binding.labelThirtyValueSelected.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
