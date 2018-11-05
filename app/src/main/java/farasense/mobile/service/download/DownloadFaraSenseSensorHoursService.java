package farasense.mobile.service.download;

import android.util.Log;

import java.util.Date;
import java.util.List;

import farasense.mobile.api.RestClient;
import farasense.mobile.api.base.ErrorListener;
import farasense.mobile.api.base.RestError;
import farasense.mobile.api.base.SuccessListener;
import farasense.mobile.model.DAO.FaraSenseSensorHoursDAO;
import farasense.mobile.model.realm.FaraSenseSensorHours;
import farasense.mobile.service.base.BaseService;
import farasense.mobile.service.listener.OnDownloadContentListener;

public class DownloadFaraSenseSensorHoursService extends BaseService {

    public static void download(OnDownloadContentListener onDownloadContentListener, Date startDate, Date finalDate) {

        new Thread(() -> RestClient.getInstance().getFaraSenseSensorDaily(new SuccessListener<List<FaraSenseSensorHours>>() {
            @Override
            public void onSuccess(List<FaraSenseSensorHours> response) {
                Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_OK);
                onDownloadContentListener.onSucess();
                FaraSenseSensorHoursDAO.saveFromServer(response);

            }
        }, new ErrorListener() {
            @Override
            public void onError(RestError restError) {
                Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_ERROR);
                onDownloadContentListener.onFail();
            }
        }, startDate, finalDate)).start();

    }






}
