package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import farasense.mobile.R;
import farasense.mobile.databinding.HourChartConsumptionFragmentDataBinding;
import farasense.mobile.util.ChartUtil;
import farasense.mobile.util.DateUtil;
import farasense.mobile.view_model.HourChartConsumptionFragmentViewModel;

public class HourChartConsumptionFragment extends Fragment {

    private HourChartConsumptionFragmentDataBinding binding;
    private HourChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hour_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(HourChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        populateHourChart();

        binding.setHourCompumptionFragment(viewModel);

        return view;
    }

    private void populateHourChart() {
        // TODO: Arrumar essa bagunça de código do gráfico

        LineChart hourChart = binding.hourChartConsumption;

        hourChart.animateXY(3000, 3000);

        XAxis xAxis = hourChart.getXAxis();
        YAxis yAxis = hourChart.getAxisLeft();

        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(true);

        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawZeroLine(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1F);

        List<Entry> entryList = viewModel.getHoursPerDayConsumption();

        xAxis.setValueFormatter(ChartUtil.setChartLabels(viewModel.getHourChartLabels()));

        LineDataSet dataSet = new LineDataSet(entryList, "Killowatts");
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setCircleColor(R.color.colorPrimaryDark);

        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);

        dataSet.setFillDrawable(drawable);

        LineData data = new LineData(dataSet);

        hourChart.setData(data);
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
