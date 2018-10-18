package farasense.mobile.view_model.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;

import farasense.mobile.service.download.DownloadFaraSenseSensorService;

public class BaseObservableViewModel extends BaseObservable {

    protected Context context;

    public static void stopServices(Context context) {
        Intent intent = new Intent(context, DownloadFaraSenseSensorService.class); context.startService(intent);
    }

}