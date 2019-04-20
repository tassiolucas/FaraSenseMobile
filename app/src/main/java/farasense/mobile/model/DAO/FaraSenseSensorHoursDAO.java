package farasense.mobile.model.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.model.realm.FaraSenseSensorHours;
import farasense.mobile.util.DateUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class FaraSenseSensorHoursDAO extends BaseDAO {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_DATE = "date";

    private static List<FaraSenseSensorHours> savedFromServer;
    private static List<FaraSenseSensorHours> dailyCompumption;

    public static List<FaraSenseSensorHours> saveFromServer(final List<FaraSenseSensorHours> response) {
        Realm realm = null;
        savedFromServer = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                if (response != null) {

                    for (FaraSenseSensorHours faraSenseSensorHours : response) {
                        faraSenseSensorHours.setTimestamp(faraSenseSensorHours.getDate().getTime());
                        savedFromServer.add(faraSenseSensorHours);
                    }

                    realm1.copyToRealmOrUpdate(savedFromServer);
                } else { savedFromServer = null; }
            });
        } finally {
            if (realm != null) { realm.close(); }
        }

        return savedFromServer;
    }

    public static List<FaraSenseSensorHours> getDailyComsumption() {
        Realm realm = null;
        dailyCompumption = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Date now = DateUtil.INSTANCE.getNow();
                    Date firtsMomentInTheDay = DateUtil.INSTANCE.getFirstMomentOfTheDay();

                    RealmResults<FaraSenseSensorHours> results = realm.where(FaraSenseSensorHours.class)
                            .between(FIELD_DATE, firtsMomentInTheDay, now)
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
