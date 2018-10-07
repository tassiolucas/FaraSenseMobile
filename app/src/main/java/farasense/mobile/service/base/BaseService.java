package farasense.mobile.service.base;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

public class BaseService extends Service {

    protected static int SLEEP_WAIT = 300000;
    protected static int SLEEP_TRY_AGAIN = 5000;
    protected static int SLEEP_TRY_AGAIN_DOUBLE = 10000;
    protected static String LOG_DSERVICE_OK = "DOWNLOAD ;) Serviço realizado com sucesso!";
    protected static String LOG_DSERVICE_ERROR = "DOWNLOAD :( Serviço falhou...";
    protected static String LOG_USERVICE_OK = "UPLOAD ;) Serviço realizado com sucesso!";
    protected static String LOG_USERVICE_ERROR = "UPLOAD :( Serviço falhou...";

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
}
