package farasense.mobile.model.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.util.DateUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class FaraSenseSensorDAO extends BaseDAO {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_DATE = "date";

    private static List<FaraSenseSensor> savedFromServer;
    private static List<FaraSenseSensor> dailyCompumption;
    private static List<FaraSenseSensor> hourMeasures;

    public static List<FaraSenseSensor> saveFromServer(final List<FaraSenseSensor> response) {
        Realm realm = null;
        savedFromServer = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                if (response != null) {

                    for (FaraSenseSensor faraSenseSensor : response) {
                        faraSenseSensor.setTimestamp(faraSenseSensor.getDate().getTime());
                        savedFromServer.add(faraSenseSensor);
                    }

                    realm1.copyToRealmOrUpdate(savedFromServer);
                } else { savedFromServer = null; }
            });
        } finally {
            if (realm != null) { realm.close(); }
        }

        return savedFromServer;
    }

    public static List<FaraSenseSensor> getDailyComsumption() {
        Realm realm = null;
        dailyCompumption = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Date now = DateUtil.getNow();
                    Date beginningDay = DateUtil.getFirtsThirtyDayInPast();

                    RealmResults<FaraSenseSensor> results = realm.where(FaraSenseSensor.class)
                            .between(FIELD_TIMESTAMP, beginningDay, now)
                            .findAll();

                    if (results.size() != 0) {
                        dailyCompumption = realm.copyFromRealm(results);
                    } else {
                        dailyCompumption = null;
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
            return dailyCompumption;
        }
    }

    public static List<FaraSenseSensor> getByIntervalsHourMeasures(Date startIntervalHour, Date endIntervalHour) {
        Realm realm = null;
        hourMeasures = new ArrayList<>();

        try{
         realm = Realm.getDefaultInstance();
         realm.executeTransaction(new Realm.Transaction() {
             @Override
             public void execute(Realm realm) {
                 RealmResults<FaraSenseSensor> results = realm.where(FaraSenseSensor.class)
                         .between(FIELD_DATE, startIntervalHour, endIntervalHour)
                         .findAll()
                         .sort(FIELD_DATE);

                 if (results.size() != 0) {
                     hourMeasures = realm.copyFromRealm(results);
                 } else {
                     hourMeasures = null;
                 }
             }
         });
        } finally {
            if (realm != null) {
                realm.close();
            }
            return hourMeasures;
        }

    }

}
