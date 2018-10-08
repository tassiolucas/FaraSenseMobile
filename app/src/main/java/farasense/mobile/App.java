package farasense.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import farasense.mobile.service.download.DownloadFaraSenseSensorService;
import farasense.mobile.view_model.base.BaseViewObservableModel;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends AppCompatActivity {

    private static App instance;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

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

        checkStoragePermissions(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseViewObservableModel.stopServices(getApplicationContext());
    }

    public void startServices() {
        Intent intent = new Intent(getApplicationContext(), DownloadFaraSenseSensorService.class); getApplicationContext().startService(intent);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void checkStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
