package farasense.mobile.model.DAO;

import java.util.ArrayList;
import java.util.List;

import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import io.realm.Realm;

public class FaraSenseSensorDAO extends BaseDAO {

    private static final String FIELD_ID = "id";

    private static List<FaraSenseSensor> savedFromServer;

    public static List<FaraSenseSensor> saveFromServer(final List<FaraSenseSensor> response) {
        Realm realm = null;
        savedFromServer = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (response != null) {
                        realm.copyToRealmOrUpdate(response);
                    } else { savedFromServer = null; }
                }
            });
        } finally {
            if (realm != null) { realm.close(); }
        }

        return savedFromServer;
    }



}
