package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mikephil.charting.data.Entry;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.util.DateUtil;
import farasense.mobile.util.EnergyUtil;

public class FiveChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> xAxisLabels = new ArrayList<>();

    FiveChartConsumptionFragmentViewModel(Application application) { super(application); }

    public List<Entry> getHourPerFiveMinutes() {
        int indexIntervals = 0;
        List<Entry> entriesMeasures = new ArrayList<>();
        List<Double> measuresList = new ArrayList<>();
        List<Interval> intervalsFiveMinutes = DateUtil.getAllIntervalsLast2Hours();
        xAxisLabels = new ArrayList<>();

        do {
            List<FaraSenseSensor> sensorMeasureList = FaraSenseSensorDAO.getMeasureByIntervals(
                    intervalsFiveMinutes.get(indexIntervals).getStart().toDate(),
                    intervalsFiveMinutes.get(indexIntervals).getEnd().toDate()
            );

            if (sensorMeasureList != null) {
                Double totalPowerMeasure = 0.0;
                for (FaraSenseSensor measure : sensorMeasureList) {
                    totalPowerMeasure = totalPowerMeasure + measure.getPower();
                }

                Double measure = EnergyUtil.getKwhInPeriod(
                        totalPowerMeasure,
                        sensorMeasureList.size(),
                        intervalsFiveMinutes.get(indexIntervals).getStart().toDate(),
                        intervalsFiveMinutes.get(indexIntervals).getEnd().toDate()
                );

                measuresList.add(measure);
            } else {
                measuresList.add(Double.valueOf(0));
            }

            if (intervalsFiveMinutes.get(indexIntervals).getEnd().getMinuteOfHour() < 10) {
                xAxisLabels.add(String.valueOf(intervalsFiveMinutes.get(indexIntervals).getEnd().getHourOfDay()) + ":0" + intervalsFiveMinutes.get(indexIntervals).getEnd().getMinuteOfHour()+"H");
            } else {
                xAxisLabels.add(String.valueOf(intervalsFiveMinutes.get(indexIntervals).getEnd().getHourOfDay()) + ":" + intervalsFiveMinutes.get(indexIntervals).getEnd().getMinuteOfHour()+"H");
            }

            indexIntervals++;
        } while (indexIntervals < intervalsFiveMinutes.size());

        Collections.reverse(measuresList);

        int i = 0;
        for (Double measure : measuresList) {
            entriesMeasures.add(new Entry(i, measure.floatValue()));
            i++;
        }

        return entriesMeasures;
    }

    public List<String> getXAxisChartLabels() {
        Collections.reverse(xAxisLabels);
        return xAxisLabels;
    }
}
