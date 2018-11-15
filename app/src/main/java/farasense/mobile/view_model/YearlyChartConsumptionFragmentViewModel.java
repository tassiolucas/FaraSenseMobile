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

public class YearlyChartConsumptionFragmentViewModel extends AndroidViewModel {

    private List<String> yearlyChartLabels = new ArrayList<>();

    public YearlyChartConsumptionFragmentViewModel(Application application) { super(application); }

    public List<BarEntry> getYearlyConsumption() {
        int yearlyBehind = 0;
        List<BarEntry> entriesMeasures = new ArrayList<>();
        List<Double> measureList = new ArrayList<>();
        List<Interval> intervals10Year = DateUtil.getAllIntervalsLast10Year();

        do {
            List<FaraSenseSensorDaily> sensorMeasuresList = FaraSenseSensorDailyDAO.getMeasureByIntervals(
                    intervals10Year.get(yearlyBehind).getStart().toDate(),
                    intervals10Year.get(yearlyBehind).getEnd().toDate()
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

            yearlyChartLabels.add(String.valueOf(intervals10Year.get(yearlyBehind).getStart().getYear()));

            yearlyBehind++;
        } while (yearlyBehind < intervals10Year.size());

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

    public List<String> getMonthlyChartLabels() {
        Collections.reverse(yearlyChartLabels);
        return yearlyChartLabels;
    }
}
