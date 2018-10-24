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

public class ThirtyChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> hourThirtyLabels = new ArrayList<>();

    ThirtyChartConsumptionFragmentViewModel(Application application) { super(application); }

    public List<Entry> getThirtyMinutesPer12Hours() {
        int indexIntervals = 0;
        List<Entry> entriesMeasures = new ArrayList<>();
        List<Double> measuresList = new ArrayList<>();
        List<Interval> intervalsThirtyMinutes = DateUtil.getAllIntervalsLast12Hours();
        hourThirtyLabels = new ArrayList<>();

        do {
           List<FaraSenseSensor> sensorMeasuresList = FaraSenseSensorDAO.getMeasureByIntervals(
                   intervalsThirtyMinutes.get(indexIntervals).getStart().toDate(),
                   intervalsThirtyMinutes.get(indexIntervals).getEnd().toDate()
           );

           if (sensorMeasuresList != null) {
               Double totalPowerMeasure = 0.0;
               for (FaraSenseSensor measure : sensorMeasuresList) {
                   totalPowerMeasure = totalPowerMeasure + measure.getPower();
               }

               Double measure = EnergyUtil.getKwhInPeriod(
                       totalPowerMeasure,
                       sensorMeasuresList.size(),
                       intervalsThirtyMinutes.get(indexIntervals).getStart().toDate(),
                       intervalsThirtyMinutes.get(indexIntervals).getEnd().toDate());

               measuresList.add(measure);
           } else {
               measuresList.add(Double.valueOf(0));
           }

           if (intervalsThirtyMinutes.get(indexIntervals).getStart().getMinuteOfHour() < 30) {
               hourThirtyLabels.add(String.valueOf(intervalsThirtyMinutes.get(indexIntervals).getEnd().getHourOfDay()) + ":30 Hs");
           } else {
               hourThirtyLabels.add(String.valueOf(intervalsThirtyMinutes.get(indexIntervals).getEnd().getHourOfDay()) + " Hs");
           }
            indexIntervals++;
        } while (indexIntervals < intervalsThirtyMinutes.size());

        Collections.reverse(measuresList);
        int i = 0;
        for (Double measure : measuresList) {
            entriesMeasures.add(new Entry(i, measure.floatValue()));
            i++;
        }

        return entriesMeasures;
    }

    public List<String> getThirtyChartLabels() {
        Collections.reverse(hourThirtyLabels);
        return hourThirtyLabels;
    }

}
