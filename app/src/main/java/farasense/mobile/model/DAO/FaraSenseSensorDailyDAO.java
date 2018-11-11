package farasense.mobile.model.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensorDaily;
import io.realm.Realm;
import io.realm.RealmResults;

public class FaraSenseSensorDailyDAO extends BaseDAO {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_DATE = "date";

    private static List<FaraSenseSensorDaily> savedFromServer;
    private static List<FaraSenseSensorDaily> dayMeasures;

    public static List<FaraSenseSensorDaily> saveFromServer(final List<FaraSenseSensorDaily> response) {
        Realm realm = null;
        savedFromServer = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {

                if (response != null) {

                    for (FaraSenseSensorDaily faraSenseSensorDaily : response) {
                        faraSenseSensorDaily.setTimestamp(faraSenseSensorDaily.getDate().getTime());
                        savedFromServer.add(faraSenseSensorDaily);
                    }

                    realm1.copyToRealmOrUpdate(savedFromServer);
                } else { savedFromServer = null; }
            });
        } finally {
            if (realm != null) { realm.close(); }
        }

        return savedFromServer;
    }

    public static List<FaraSenseSensorDaily> getMeasureByIntervals(Date startIntervalDay, Date endIntervalDay) {
        Realm realm = null;
        dayMeasures = new ArrayList<>();

        try{
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<FaraSenseSensorDaily> results = realm.where(FaraSenseSensorDaily.class)
                            .between(FIELD_DATE, startIntervalDay, endIntervalDay)
                            .findAll()
                            .sort(FIELD_DATE);

                    if (results.size() != 0) {
                        dayMeasures = realm.copyFromRealm(results);
                    } else {
                        dayMeasures = null;
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
            return dayMeasures;
        }
    }

}
