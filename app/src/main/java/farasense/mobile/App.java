package farasense.mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import farasense.mobile.service.download.DownloadFaraSenseSensorService;
import farasense.mobile.view_model.base.BaseViewObservableModel;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends AppCompatActivity {

    private static App instance;

    public static App getInstance() { return instance; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.instance = this;

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("faraSenseMobileDataBase")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        setContentView(R.layout.activity_app);

        startServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseViewObservableModel.stopServices(getApplicationContext());
    }

    public void startServices() {
        Intent intent = new Intent(getApplicationContext(), DownloadFaraSenseSensorService.class); getApplicationContext().startService(intent);
    }

}
