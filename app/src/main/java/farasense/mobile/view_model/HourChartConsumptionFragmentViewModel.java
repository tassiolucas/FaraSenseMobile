package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mikephil.charting.data.Entry;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;
import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.util.DateUtil;
import farasense.mobile.util.EnergyUtil;

public class HourChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> hourChartLabels = new ArrayList<>();

    public HourChartConsumptionFragmentViewModel(Application application) { super(application); }

    // TODO: (OK!) - FAZER UM CÓDIGO QUE LEIA AS MEDIDAS RELATIVAS AO TEMPO EM 24 HORAS
    // TODO: - FAZER UM CODIGO COM LIVEDATA

    public List<Entry> getHourPer24Hours() {
        int hoursBehind = 0;
        List<Entry> entriesMeasures = new ArrayList<>();
        List<Interval> intervals24Hours = DateUtil.getAllIntervalsLast24Hours();
        hourChartLabels = new ArrayList<>();

        do {
            List<FaraSenseSensor> sensorMeasuresList = FaraSenseSensorDAO.getMeasureByIntervals(
                    intervals24Hours.get(hoursBehind).getStart().toDate(),
                    intervals24Hours.get(hoursBehind).getEnd().toDate()
            );

            if (sensorMeasuresList != null) {
                Double totalPowerMeasure = 0.0;
                for (FaraSenseSensor measure : sensorMeasuresList) {
                    totalPowerMeasure = totalPowerMeasure + measure.getPower();
                }

                Double measure = EnergyUtil.getKwhInPeriod(
                        totalPowerMeasure,
                        sensorMeasuresList.size(),
                        intervals24Hours.get(hoursBehind).getStart().toDate(),
                        intervals24Hours.get(hoursBehind).getEnd().toDate());

                entriesMeasures.add(new Entry(hoursBehind, measure.floatValue()));
            } else {
                entriesMeasures.add(new Entry(hoursBehind, 0));
            }

            hourChartLabels.add(String.valueOf(intervals24Hours.get(hoursBehind).getEnd().getHourOfDay()) + "H");

            hoursBehind++;
        } while (hoursBehind < intervals24Hours.size());

        return entriesMeasures;
    }

    public List<String> getHourChartLabels() {
        return hourChartLabels;
    }

}
