package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.util.DateUtil;
import farasense.mobile.util.EnergyUtil;

public class HourChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> hourChartLabels = new ArrayList<>();

    public HourChartConsumptionFragmentViewModel(Application application) { super(application); }

    // TODO: FAZER UM CÓDIGO QUE LEIA AS MEDIDAS RELATIVAS AO TEMPO EM 24 HORAS

    public List<Entry> getHoursPerDayConsumption() {
        int hoursBehind = 0;
        List<Entry> entryMeasuresHourPerHour = new ArrayList<>();
        List<Interval> intervals24Hours = DateUtil.getAllIntervalsLast24Hours();
        setNewHourChartLabels();

        do {
            List<FaraSenseSensor> faraSenseSensorDailyList = FaraSenseSensorDAO.getByIntervalsHourMeasures(intervals24Hours.get(hoursBehind).getStart().toDate(), intervals24Hours.get(hoursBehind).getEnd().toDate());

            if (faraSenseSensorDailyList != null) {

                Double totalPowerMeasure = 0.0;
                for (FaraSenseSensor measure : faraSenseSensorDailyList) {
                    totalPowerMeasure = totalPowerMeasure + measure.getPower();
                }

                Double measure = EnergyUtil.getKwhInPeriod(
                        totalPowerMeasure,
                        faraSenseSensorDailyList.size(),
                        intervals24Hours.get(hoursBehind).getStart().toDate(),
                        intervals24Hours.get(hoursBehind).getEnd().toDate());

                entryMeasuresHourPerHour.add(new Entry(hoursBehind, measure.floatValue()));

            } else {
                entryMeasuresHourPerHour.add(new Entry(hoursBehind, 0));
            }

            addHourChartLabel(String.valueOf(intervals24Hours.get(hoursBehind).getEnd().getHourOfDay()) + "H");

            hoursBehind++;
        } while (hoursBehind < intervals24Hours.size());

        return entryMeasuresHourPerHour;
    }

    public List<String> getHourChartLabels() {
        Collections.reverse(hourChartLabels);
        return hourChartLabels;
    }

    public void addHourChartLabel(String label) {
        hourChartLabels.add(label);
    }

    public void setNewHourChartLabels() {
        hourChartLabels = new ArrayList<>();
    }
}
