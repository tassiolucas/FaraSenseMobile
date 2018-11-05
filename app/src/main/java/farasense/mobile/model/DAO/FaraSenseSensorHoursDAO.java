package farasense.mobile.model.DAO;

import java.util.ArrayList;
import java.util.List;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.model.realm.FaraSenseSensorHours;
import io.realm.Realm;

public class FaraSenseSensorHoursDAO extends BaseDAO {

    private static List<FaraSenseSensorHours> savedFromServer;

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

}
