package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO;
import farasense.mobile.model.DAO.FaraSenseSensorHoursDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.model.realm.FaraSenseSensorDaily;
import farasense.mobile.model.realm.FaraSenseSensorHours;
import farasense.mobile.util.DateUtil;

public class DailyChartConsumptionFragmentViewModel extends AndroidViewModel {

    public static final int FIRST_MEASURE = 0;
    private List<String> dayChartLabels = new ArrayList<>();

    public DailyChartConsumptionFragmentViewModel(Application application) { super(application); }

    public List<BarEntry> getDailyConsumpition() {
        int daysBehind = 0;
        List<BarEntry> entriesMeasures = new ArrayList<>();
        List<Double> measureList = new ArrayList<>();
        List<Interval> intervals30Days = DateUtil.getAllIntervalsLast30Days();

        do {
            List<FaraSenseSensorDaily> sensorMeasuresList = FaraSenseSensorDailyDAO.getMeasureByIntervals(
                    intervals30Days.get(daysBehind).getStart().toDate(),
                    intervals30Days.get(daysBehind).getEnd().toDate()
            );

            if (sensorMeasuresList != null) {
                Double totalKwh = 0.0;
                for (FaraSenseSensorDaily measure : sensorMeasuresList) {
                    totalKwh = totalKwh + measure.getTotalKwh();
                }

                measureList.add(totalKwh);
            } else {
                measureList.add(Double.valueOf(0));
            }

            dayChartLabels.add(String.valueOf(intervals30Days.get(daysBehind).getEnd().getDayOfMonth()) + "/" + String.valueOf(intervals30Days.get(daysBehind).getEnd().getMonthOfYear()));

            daysBehind++;
        } while (daysBehind < intervals30Days.size());

        List<FaraSenseSensorHours> todayMeauseres = FaraSenseSensorHoursDAO.getDailyComsumption();

        Double todayTotalKwh = 0D;
        if (todayMeauseres != null) {
            for (FaraSenseSensorHours todayMeausere : todayMeauseres) {
                todayTotalKwh = todayTotalKwh + todayMeausere.getKwh();
            }
        }
        measureList.set(FIRST_MEASURE, todayTotalKwh);

        Collections.reverse(measureList);

        int i = 0;
        for (Double measure : measureList) {
            entriesMeasures.add(i, new BarEntry(
                    Float.valueOf(i),
                    Float.valueOf(measure.floatValue())
            ));
            i++;
        }

        return entriesMeasures;
    }

    public List<String> getDailyChartLabels() {
        Collections.reverse(dayChartLabels);
        return dayChartLabels;
    }

}
