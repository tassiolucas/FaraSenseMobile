package farasense.mobile.view_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import farasense.mobile.service.download.DownloadFaraSenseSensorService;
import farasense.mobile.view.ui.activity.DashboardActivity;
import farasense.mobile.view.ui.activity.base.BaseActivity;
import farasense.mobile.view_model.base.BaseObservableViewModel;

public class SplashViewModel extends BaseObservableViewModel {

    public SplashViewModel (Context context) { this.context = context; }

    public void startServices() {
        Intent intent = new Intent(context, DownloadFaraSenseSensorService.class); context.startService(intent);
    }

    public void goToDashBoardActivity() {
        startServices();
        BaseActivity.startActivity((Activity) context, DashboardActivity.class, true);
    }

}
