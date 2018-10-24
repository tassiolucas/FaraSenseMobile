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

public class HourChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> hourChartLabels = new ArrayList<>();

    public HourChartConsumptionFragmentViewModel(Application application) { super(application); }

    // TODO: (OK!) - FAZER UM CÓDIGO QUE LEIA AS MEDIDAS RELATIVAS AO TEMPO EM 24 HORAS
    // TODO: -(OK!) INVERTER A PLOTAGEM DOS GRÁFICOS
    // TODO: - FAZER UM CODIGO COM LIVEDATA

    public List<Entry> getHourPer24Hours() {
        int hoursBehind = 0;
        List<Entry> entriesMeasures = new ArrayList<>();
        List<Double> measuresList = new ArrayList<>();
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
                        sensorMeasuresList.get(0).getDate(),
                        sensorMeasuresList.get(sensorMeasuresList.size() - 1).getDate());

                measuresList.add(measure);
            } else {
                measuresList.add(Double.valueOf(0));
            }

            hourChartLabels.add(String.valueOf(intervals24Hours.get(hoursBehind).getEnd().getHourOfDay()) + "H");

            hoursBehind++;
        } while (hoursBehind < intervals24Hours.size());

        Collections.reverse(measuresList);
        int i = 0;
        for (Double measure : measuresList) {
            entriesMeasures.add(new Entry(i, measure.floatValue()));
            i++;
        }

        return entriesMeasures;
    }


    public List<String> getHourChartLabels() {
        Collections.reverse(hourChartLabels);
        return hourChartLabels;
    }

}
