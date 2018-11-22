package farasense.mobile.view.ui.activity.fragment;

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
import farasense.mobile.databinding.FiveChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.view_model.FiveChartConsumptionFragmentViewModel;

public class FiveChartConsumptionFragment extends Fragment {

    private FiveChartConsumptionFragmentDataBinding binding;
    private FiveChartConsumptionFragmentViewModel viewModel;
    private LineChart fiveChart;
    private List<Entry> entryList;
    private LineDataSet dataSet;
    private LineData data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_five_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(FiveChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        createFiveChart();

        binding.setFiveCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        fiveChart.animateY( 3000);
        fiveChart.invalidate();
    }

    private void createFiveChart() {
        fiveChart = binding.fiveChartConsumption;
        fiveChart.getDescription().setText(getResources().getString(R.string.description_five_chart));
        fiveChart.setScaleYEnabled(false);

        configureValueSelectedListener(fiveChart);

        XAxis xAxis = fiveChart.getXAxis();
        YAxis yAxis = fiveChart.getAxisLeft();

        // Popula as entradas e os labels
        entryList = viewModel.getHourPerFiveMinutes();
        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getXAxisChartLabels()));

        // Configura os eixos
        configureAxis(fiveChart, xAxis, yAxis);

        dataSet = configureDataSet(entryList); // Prepara os dados para gráfico

        data = new LineData(dataSet);

        fiveChart.setData(data); // Posta os dados no gráfico
    }

    private void configureAxis(LineChart hourChart, XAxis xAxis, YAxis yAxis) {
        hourChart.animateY(3000);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setEnabled(true);

        hourChart.getAxisRight().setEnabled(false);
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
        dataSet.setColor(R.color.colorFiveChartLine);
        dataSet.setCircleColor(R.color.colorFiveChartPoint);
        dataSet.setDrawValues(false);

        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_five_chart);

        dataSet.setFillDrawable(drawable);
        return dataSet;
    }

    private void configureValueSelectedListener(LineChart hourChart) {
        hourChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                if (binding.labelFiveValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelFiveValueSelected.setText(String.valueOf(entry.getY()));
                } else {
                    binding.labelFiveValueSelected.setVisibility(View.VISIBLE);
                    binding.labelFiveValueSelected.setText(String.valueOf(entry.getY()));
                }
            }

            @Override
            public void onNothingSelected() {
                if (binding.labelFiveValueSelected.getVisibility() == View.VISIBLE) {
                    binding.labelFiveValueSelected.setVisibility(View.GONE);
                } else {
                    binding.labelFiveValueSelected.setVisibility(View.GONE);
                }
            }
        });
    }
}
