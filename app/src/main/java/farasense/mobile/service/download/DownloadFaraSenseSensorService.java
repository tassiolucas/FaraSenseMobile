package farasense.mobile.service.download;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import farasense.mobile.api.RestClient;
import farasense.mobile.api.base.ErrorListener;
import farasense.mobile.api.base.RestError;
import farasense.mobile.api.base.SuccessListener;
import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.DAO.base.BaseDAO;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.service.base.BaseService;
import farasense.mobile.util.ConnectionUtil;

public class DownloadFaraSenseSensorService extends BaseService {

    private final class FaraSenseSensorServiceHandler extends Handler {

        public FaraSenseSensorServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            while (!shouldStop) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
                cal.set(Calendar.HOUR, 12);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.clear(Calendar.MONTH);
                cal.set(Calendar.DAY_OF_MONTH, 0);

                try {
                    if (ConnectionUtil.isDataConnectionAvailable(getApplicationContext())) {
                        RestClient.getInstance().getFaraSenseSensor(new SuccessListener<List<FaraSenseSensor>>() {
                            @Override
                            public void onSuccess(List<FaraSenseSensor> response) {
                                Log.d("FARASENSE SENSOR", LOG_DSERVICE_OK);
                                FaraSenseSensorDAO.saveFromServer(response);
                                BaseDAO.saveDataBase(getApplicationContext());
                            }
                        }, new ErrorListener() {
                            @Override
                            public void onError(RestError restError) {
                                Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR);
                            }
                        }, cal.getTime()
                        , new Date());
                        Thread.sleep(SLEEP_WAIT);
                    } else {
                        Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR);
                        Thread.sleep(SLEEP_TRY_AGAIN);
                    }
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread threadFaraSenseSensor = new HandlerThread("FaraSenseSensorStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        threadFaraSenseSensor.start();
        looper = threadFaraSenseSensor.getLooper();
        handler = new FaraSenseSensorServiceHandler(looper);
    }
}
