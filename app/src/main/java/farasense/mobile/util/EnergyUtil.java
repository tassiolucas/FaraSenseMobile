package farasense.mobile.util;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;
import java.util.List;

import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO;
import farasense.mobile.model.realm.FaraSenseSensorDaily;

public class EnergyUtil {

    public static final Double EMPTY_MEASURE = 0.0;

    // TODO: Verificar a relação entre horas e o corte do servidor

    public static Double getKwhInPeriod(Double totalPowerMeasure, int quantMeasure, Date startDate, Date endDate) {
        final int HOUR_MINUTES = 60;
        final int KILO = 1000;

        Double mediaPowerMeasure = totalPowerMeasure / quantMeasure;

        DateTime startPeriod = new DateTime(startDate).withZone(DateUtil.getTimeZoneBrazil());
        DateTime endPeriod = new DateTime(endDate).withZone(DateUtil.getTimeZoneBrazil());

        Interval interval = new Interval(startPeriod, endPeriod);

        Double hour = Double.valueOf(interval.toDuration().getStandardMinutes()) / HOUR_MINUTES;

        Double wh = mediaPowerMeasure * hour;

        Double kwh = wh / KILO;

        return kwh;
    }

    public static Double getValueCost(DateTime maturityDate, Float rateKhw, Float rateFlag) {
        Double totalCost = 0.0;
        DateTime endPeriod = maturityDate;
        DateTime startPeriod = DateUtil.get30DaysAgo(endPeriod);

        List<FaraSenseSensorDaily> faraSenseSensorDailyList = FaraSenseSensorDailyDAO.getMeasureByIntervals(startPeriod.toDate(), endPeriod.toDate());

        Double measure = 0.0;
        for(FaraSenseSensorDaily faraSenseSensorDaily : faraSenseSensorDailyList) {
            measure = measure + faraSenseSensorDaily.getTotalKwh();
        }

        totalCost = measure * rateKhw;
        totalCost = totalCost + rateFlag;

        return totalCost;
    }

}
