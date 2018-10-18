package farasense.mobile;

import android.app.Activity;
import android.app.Application;

import farasense.mobile.view_model.base.BaseObservableViewModel;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static App instance;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Activity currentActivity;
    private boolean downloadingServices;

    public static App getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("faraSenseMobileDataBase")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        downloadingServices = false;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

}
