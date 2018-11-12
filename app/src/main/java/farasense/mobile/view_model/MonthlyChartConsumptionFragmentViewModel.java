package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO;
import farasense.mobile.model.realm.FaraSenseSensorDaily;
import farasense.mobile.util.DateUtil;

public class MonthlyChartConsumptionFragmentViewModel extends AndroidViewModel {

    public static final int FIRST_MEASURE = 0;
    private List<String> monthlyChartLabels = new ArrayList<>();

    public MonthlyChartConsumptionFragmentViewModel(Application application) { super(application); }

    public List<BarEntry> getMonthlyConsumption() {
        int monthlyBehind = 0;
        List<BarEntry> entriesMeasures = new ArrayList<>();
        List<Double> measureList = new ArrayList<>();
        List<Interval> intervals12Monthly = DateUtil.getAllIntervalsLast12Monthly();

        do {
            List<FaraSenseSensorDaily> sensorMeasuresList = FaraSenseSensorDailyDAO.getMeasureByIntervals(
                    intervals12Monthly.get(monthlyBehind).getStart().toDate(),
                    intervals12Monthly.get(monthlyBehind).getStart().toDate()
            );

            if (sensorMeasuresList != null) {
                Double totalKhm = 0.0;
                for (FaraSenseSensorDaily measure: sensorMeasuresList) {
                    totalKhm = totalKhm + measure.getTotalKwh();
                }

                measureList.add(totalKhm);
            } else {
                measureList.add(Double.valueOf(0));
            }

            monthlyChartLabels.add(String.valueOf(intervals12Monthly.get(monthlyBehind).getEnd().getDayOfMonth()));

        } while (monthlyBehind < intervals12Monthly.size());

        return entriesMeasures;

    }

    public List<String> getMonthlyChartLabels() {
        Collections.reverse(monthlyChartLabels);
        return monthlyChartLabels;
    }






}
