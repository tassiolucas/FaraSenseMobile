package farasense.mobile.service.base;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import farasense.mobile.service.download.DownloadFaraSenseSensorDailyService;
import farasense.mobile.service.download.DownloadFaraSenseSensorHoursService;
import farasense.mobile.service.download.DownloadFaraSenseSensorService;
import farasense.mobile.service.listener.OnDownloadContentListener;
import farasense.mobile.service.listener.OnStartServiceDownload;
import farasense.mobile.util.DateUtil;

public class BaseService extends Service {

    public static final int totalInitialsServices = 3;
    protected static final int SLEEP_WAIT = 300000;
    protected static final int SLEEP_TRY_AGAIN = 5000;
    protected static final int SLEEP_TRY_AGAIN_DOUBLE = 10000;
    protected static final String LOG_DSERVICE_OK = "DOWNLOAD ;) Serviço realizado com sucesso!";
    protected static final String LOG_DSERVICE_ERROR = "DOWNLOAD :( Serviço falhou...";
    protected static final String LOG_USERVICE_OK = "UPLOAD ;) Serviço realizado com sucesso!";
    protected static final String LOG_USERVICE_ERROR = "UPLOAD :( Serviço falhou...";

    protected Looper looper;
    protected Message msg;
    protected boolean shouldStop;
    protected Handler handler;

    @Override
    public void onCreate() {
        shouldStop = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf(msg.arg1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        msg = handler.obtainMessage();
        msg.arg1 = startId;
        handler.sendMessage(msg);
        return START_STICKY;
    }

    public static void initialDownloadDataSensors(final OnStartServiceDownload onStartServiceDownload) {
        final int[] finishedServices = {0};
        onStartServiceDownload.onStart();

        DownloadFaraSenseSensorService.download(new OnDownloadContentListener() {
            @Override
            public void onSucess() {
                finishedServices[0]++;

                if (finishedServices[0] >= totalInitialsServices) {
                    onStartServiceDownload.onFinish();
                }
            }

            @Override
            public void onFail() {
                onStartServiceDownload.onFail();

            }
        }, DateUtil.getFirts24Hours(), DateUtil.getNow());

        DownloadFaraSenseSensorHoursService.download(new OnDownloadContentListener() {
            @Override
            public void onSucess() {
                finishedServices[0]++;

                if (finishedServices[0] >= totalInitialsServices) {
                    onStartServiceDownload.onFinish();
                }
            }

            @Override
            public void onFail() {
                onStartServiceDownload.onFail();
            }
        }, DateUtil.getFirts24HoursWithMinutesReset(), DateUtil.getNow());

        DownloadFaraSenseSensorDailyService.download(new OnDownloadContentListener() {
            @Override
            public void onSucess() {
                finishedServices[0]++;

                if (finishedServices[0] >= totalInitialsServices) {
                    onStartServiceDownload.onFinish();
                }
            }

            @Override
            public void onFail() {
                onStartServiceDownload.onFail();;
            }
        }, DateUtil.getFirts30Days(), DateUtil.getNow());

    }
}
