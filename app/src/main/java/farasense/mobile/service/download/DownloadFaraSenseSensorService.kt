package farasense.mobile.service.download

import android.content.Context
import android.os.*
import android.util.Log
import farasense.mobile.api.RestClient
import farasense.mobile.api.base.ErrorListener
import farasense.mobile.api.base.RestError
import farasense.mobile.api.base.SuccessListener
import farasense.mobile.model.DAO.FaraSenseSensorDAO
import farasense.mobile.model.realm.FaraSenseSensor
import farasense.mobile.service.base.BaseService
import farasense.mobile.service.listener.OnDownloadContentListener
import farasense.mobile.util.ConnectionUtil
import farasense.mobile.util.DateUtil
import farasense.mobile.util.PermissionUtil
import java.util.*

class DownloadFaraSenseSensorService : BaseService() {

    private inner class FaraSenseSensorServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            while (!shouldStop) {
                try {
                    if (ConnectionUtil.isDataConnectionAvailable(applicationContext)) {
                        RestClient.getFaraSenseSensor(applicationContext, object : SuccessListener<List<FaraSenseSensor>>() {
                            override fun onSuccess(response: List<FaraSenseSensor>) {
                                Log.d("FARASENSE SENSOR", LOG_DSERVICE_OK)
                                FaraSenseSensorDAO().saveFromServer(response)
                                if (PermissionUtil.hasPermissions(applicationContext, *PermissionUtil.PERMISSIONS)) {
                                    // BaseDAO().saveDataBase(applicationContext)
                                }
                            }
                        }, object : ErrorListener() {
                            override fun onError(restError: RestError) {
                                Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
                            }
                        }, DateUtil.firstMomentOfTheDay, DateUtil.now)
                        Thread.sleep(BaseService.SLEEP_WAIT.toLong())
                    } else {
                        Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
                        Thread.sleep(BaseService.SLEEP_TRY_AGAIN.toLong())
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }

            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        val threadFaraSenseSensor = HandlerThread("FaraSenseSensorStartArguments", Process.THREAD_PRIORITY_BACKGROUND)
        threadFaraSenseSensor.start()
        looper = threadFaraSenseSensor.looper
        handler = FaraSenseSensorServiceHandler(looper!!)
    }

    companion object {

        fun download(context: Context, onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {
            Thread {
                RestClient.getFaraSenseSensor(context, object : SuccessListener<List<FaraSenseSensor>>() {
                    override fun onSuccess(response: List<FaraSenseSensor>) {
                        Log.d("FARASENSE SENSOR", LOG_DSERVICE_OK)
                        onDownloadContentListener.onSucess()
                        FaraSenseSensorDAO().saveFromServer(response)
                    }
                }, object : ErrorListener() {
                    override fun onError(restError: RestError) {
                        Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
                        onDownloadContentListener.onFail()
                    }
                }, startDate, finalDate)
            }.start()
        }
    }
}
