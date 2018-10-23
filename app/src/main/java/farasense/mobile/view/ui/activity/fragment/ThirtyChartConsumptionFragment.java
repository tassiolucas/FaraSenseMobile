package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

        createHourChart();

        binding.setThirtyCompumptionFragment(viewModel);

        return view;
    }

    private void createHourChart() {
        thirtyChart = binding.thirtyChartConsumption;
        XAxis xAxis = thirtyChart.getXAxis();
        YAxis yAxis = thirtyChart.getAxisLeft();
        LineDataSet dataSet;
        LineData data;

        // Popula as entradas e os labels
        List<Entry> entryList = viewModel.getThirtyMinutesPer12Hours();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getThirtyChartLabels()));

        configureAxis(thirtyChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList);

        data = new LineData(dataSet);

        thirtyChart.setData(data);
    }

    private void configureAxis(LineChart hourChart, XAxis xAxis, YAxis yAxis) {
        hourChart.animateXY(3000, 3000);

        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(true);

        yAxis.setDrawLabels(false);
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
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setCircleColor(R.color.colorPrimaryDark);

        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);

        dataSet.setFillDrawable(drawable);
        return dataSet;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
