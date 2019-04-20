package farasense.mobile.service.base

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import farasense.mobile.service.download.DownloadFaraSenseSensorDailyService
import farasense.mobile.service.download.DownloadFaraSenseSensorHoursService
import farasense.mobile.service.download.DownloadFaraSenseSensorService
import farasense.mobile.service.listener.OnDownloadContentListener
import farasense.mobile.service.listener.OnStartServiceDownload
import farasense.mobile.util.DateUtil

@SuppressLint("Registered")
open class BaseService : Service() {

    protected var looper: Looper? = null
    protected lateinit var msg: Message
    protected var shouldStop: Boolean = false
    protected var handler: Handler? = null

    override fun onCreate() {
        shouldStop = false
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf(msg.arg1)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        msg = handler!!.obtainMessage()
        msg.arg1 = startId
        handler!!.sendMessage(msg)
        return START_STICKY
    }

    companion object {

        val totalInitialsServices = 3
        const val SLEEP_WAIT = 300000
        const val SLEEP_TRY_AGAIN = 5000
        const val SLEEP_TRY_AGAIN_DOUBLE = 10000
        const val LOG_DSERVICE_OK = "DOWNLOAD ;) Serviço realizado com sucesso!"
        const val LOG_DSERVICE_ERROR = "DOWNLOAD :( Serviço falhou..."
        const val LOG_USERVICE_OK = "UPLOAD ;) Serviço realizado com sucesso!"
        const val LOG_USERVICE_ERROR = "UPLOAD :( Serviço falhou..."

        fun initialDownloadDataSensors(onStartServiceDownload: OnStartServiceDownload) {
            val finishedServices = intArrayOf(0)
            onStartServiceDownload.onStart()

            DownloadFaraSenseSensorService.download(object : OnDownloadContentListener {
                override fun onSucess() {
                    finishedServices[0]++

                    if (finishedServices[0] >= totalInitialsServices) {
                        onStartServiceDownload.onFinish()
                    }
                }

                override fun onFail() {
                    onStartServiceDownload.onFail()

                }
            }, DateUtil.firts24Hours, DateUtil.now)

            DownloadFaraSenseSensorHoursService.download(object : OnDownloadContentListener {
                override fun onSucess() {
                    finishedServices[0]++

                    if (finishedServices[0] >= totalInitialsServices) {
                        onStartServiceDownload.onFinish()
                    }
                }

                override fun onFail() {
                    onStartServiceDownload.onFail()
                }
            }, DateUtil.firts24HoursWithMinutesReset, DateUtil.now)

            DownloadFaraSenseSensorDailyService.download(object : OnDownloadContentListener {
                override fun onSucess() {
                    finishedServices[0]++

                    if (finishedServices[0] >= totalInitialsServices) {
                        onStartServiceDownload.onFinish()
                    }
                }

                override fun onFail() {
                    onStartServiceDownload.onFail()
                }
            }, DateUtil.firts30Days, DateUtil.now)

        }
    }
}
