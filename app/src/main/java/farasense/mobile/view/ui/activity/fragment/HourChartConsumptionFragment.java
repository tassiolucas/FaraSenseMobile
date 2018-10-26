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
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;
import javax.annotation.Nullable;

import farasense.mobile.R;
import farasense.mobile.databinding.HourChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.view_model.HourChartConsumptionFragmentViewModel;

public class HourChartConsumptionFragment extends Fragment {

    private HourChartConsumptionFragmentDataBinding binding;
    private HourChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private LineChart hourChart;
    private List<Entry> entryList;
    private LineDataSet dataSet;
    private LineData data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hour_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(HourChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        createHourChart();

        binding.setHourCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        hourChart.animateY( 3000);
        hourChart.invalidate();
    }

    private void createHourChart() {
        hourChart = binding.hourChartConsumption;
        hourChart.getDescription().setText(getResources().getString(R.string.description_hour_chart));
        hourChart.setScaleYEnabled(false);

        configureValueSelectedListener(hourChart);

        XAxis xAxis = hourChart.getXAxis();
        YAxis yAxis = hourChart.getAxisLeft();

        // Popula as entradas e os labels
        entryList = viewModel.getHourPer24Hours();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getHourChartLabels()));

        // Configura os eixos
        configureAxis(hourChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList); // Prepara os dados para gráfico

        data = new LineData(dataSet);

        hourChart.setData(data); // Posta os dados no gráfico
    }

    private void configureAxis(LineChart hourChart, XAxis xAxis, YAxis yAxis) {
        hourChart.animateY(3000);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setEnabled(true);

        hourChart.getAxisRight().setEnabled(false);
        yAxis.setDrawLabels(true);
        yAxis.setDrawZeroLine(false);
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
        dataSet.setColor(R.color.colorHourChartLine);
        dataSet.setCircleColor(R.color.colorHourChartPoint);
        dataSet.setDrawValues(false);

        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_hour_chart);

        dataSet.setFillDrawable(drawable);
        return dataSet;
    }

    private void configureValueSelectedListener(LineChart hourChart) {
        hourChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                if (binding.labelHourValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelHourValueSelected.setText(String.valueOf(entry.getY()));
                } else {
                    binding.labelHourValueSelected.setVisibility(View.VISIBLE);
                    binding.labelHourValueSelected.setText(String.valueOf(entry.getY()));
                }
            }

            @Override
            public void onNothingSelected() {
                if (binding.labelHourValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelHourValueSelected.setVisibility(View.GONE);
                } else {
                    binding.labelHourValueSelected.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
