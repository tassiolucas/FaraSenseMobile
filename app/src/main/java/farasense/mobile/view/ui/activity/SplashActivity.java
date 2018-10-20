package farasense.mobile.view.ui.activity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import butterknife.ButterKnife;
import farasense.mobile.App;
import farasense.mobile.R;
import farasense.mobile.databinding.SplashDataBinding;
import farasense.mobile.service.base.BaseService;
import farasense.mobile.service.listener.OnStartServiceDownload;
import farasense.mobile.util.ConnectionUtil;
import farasense.mobile.util.PermissionUtil;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view_model.SplashViewModel;
import farasense.mobile.view_model.base.BaseObservableViewModel;

public class SplashActivity extends BaseActivity {

    private static final int TAG_RESULT_PERMISSION_OK = 1;
    private SplashDataBinding binding;
    private SplashViewModel viewModel;
    private boolean shouldStopService = true;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TAG_RESULT_PERMISSION_OK: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initScreen();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    public void initScreen() {
        viewModel = new SplashViewModel(this);
        binding.setSplash(viewModel);

        if (ConnectionUtil.isDataConnectionAvailable(getApplicationContext())) {
            downloadContent();
            // Para agilizar a programação...
            // TODO: Remover após desenvolvimento
            //viewModel.goToDashBoardActivity();
        } else {
            Toast.makeText(this, R.string.error_no_connection_message, Toast.LENGTH_SHORT);
            viewModel.goToDashBoardActivity();
        }
    }

    private void downloadContent() {
        BaseService.initialDownloadDataSensors(new OnStartServiceDownload() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                runOnUiThread(() ->
                        viewModel.goToDashBoardActivity()
                );
                shouldStopService = false;
            }

            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), R.string.error_downloading_message, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);

        if (!PermissionUtil.hasPermissions(this, PermissionUtil.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSIONS, PermissionUtil.PERMISSION_ALL);
        } else {
            initScreen();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shouldStopService) {
            BaseObservableViewModel.stopServices(this);
        }
    }
}
