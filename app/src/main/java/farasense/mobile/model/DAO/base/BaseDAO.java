package farasense.mobile.model.DAO.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.internal.IOException;

public class BaseDAO {

    public static RealmObject save(final RealmObject realmObject) {
        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(realmObject);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
        return realmObject;
    }

    public static <T extends RealmObject> boolean deleteAll(final Class<T> clazz) {
        Realm realm = null;
        final boolean[] deleted = new boolean[1];
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(clazz);
                    deleted[0] = true;
                }
            });
        } catch (Exception e) {
            deleted[0] = false;
        }
        finally {
            if(realm != null) {
                realm.close();
            }
        }
        return deleted[0];
    }

    public static void saveDataBase(Context context) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/faraSenseDB.realm"));
            if (file.exists()) {
                file.delete();
            }

            realm.writeCopyTo(file);
            Toast.makeText(context, "Backup feito com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            realm.close();
            e.printStackTrace();
        } finally {
            if (realm != null) { realm.close(); }
        }
    }

}
