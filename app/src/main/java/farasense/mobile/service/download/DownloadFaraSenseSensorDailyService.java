package farasense.mobile.service.download;

import java.util.Date;
import java.util.List;

import farasense.mobile.api.RestClient;
import farasense.mobile.api.base.ErrorListener;
import farasense.mobile.api.base.RestError;
import farasense.mobile.api.base.SuccessListener;
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO;
import farasense.mobile.model.realm.FaraSenseSensorDaily;
import farasense.mobile.service.base.BaseService;
import farasense.mobile.service.listener.OnDownloadContentListener;

public class DownloadFaraSenseSensorDailyService extends BaseService {

    public static void download(OnDownloadContentListener onDownloadContentListener, Date startDate, Date finalDate) {

        new Thread(() -> RestClient.getInstance().getFaraSenseSensorDaily(new SuccessListener<List<FaraSenseSensorDaily>>() {
            @Override
            public void onSuccess(List<FaraSenseSensorDaily> response) {
                onDownloadContentListener.onSucess();
                FaraSenseSensorDailyDAO.saveFromServer(response);
            }
        }, new ErrorListener() {
            @Override
            public void onError(RestError restError) {

            }
        }, startDate, finalDate)).start();
    }
}
