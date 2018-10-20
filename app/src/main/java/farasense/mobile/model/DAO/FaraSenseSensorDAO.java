package farasense.mobile.model.DAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.util.DateUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class FaraSenseSensorDAO extends BaseDAO {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TIMESTAMP = "timestamp";

    private static List<FaraSenseSensor> savedFromServer;
    private static List<FaraSenseSensor> dailyCompumption;

    public static List<FaraSenseSensor> saveFromServer(final List<FaraSenseSensor> response) {
        Realm realm = null;
        savedFromServer = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                if (response != null) {
                    realm1.copyToRealmOrUpdate(response);
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
                    Date now = DateUtil.getTodayDay();
                    Date beginningDay = DateUtil.getFirtsThirtyDayInPast();

                    RealmResults<FaraSenseSensor> results = realm.where(FaraSenseSensor.class)
                            .between("timestamp", beginningDay, now)
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

}
