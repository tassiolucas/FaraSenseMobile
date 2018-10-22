package farasense.mobile.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class ChartUtil {

    public static IAxisValueFormatter setChartLabels(List<String> chartLabels) {
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return chartLabels.get((int) value);
            }
        };
        return formatter;
    }

}
