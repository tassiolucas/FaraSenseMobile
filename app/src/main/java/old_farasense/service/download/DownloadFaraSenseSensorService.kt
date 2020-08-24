//package old_farasense.service.download
//
//import android.content.Context
//import android.os.*
//import android.util.Log
//import farasense.mobile.BuildConfig
//import old_farasense.api.RestClient
//import old_farasense.api.base.RestError
//import old_farasense.api.base.SuccessListener
//import old_farasense.model.DAO.FaraSenseSensorDAO
//import old_farasense.model.DAO.base.BaseDAO
//import old_farasense.model.realm.FaraSenseSensor
//import old_farasense.service.base.BaseService
//import old_farasense.service.listener.OnDownloadContentListener
//import old_farasense.util.ConnectionUtil
//import old_farasense.util.DateUtil
//import old_farasense.util.PermissionUtil
//import old_farasense.view_model.base.BaseObservableViewModel
//import java.util.*
//
//class DownloadFaraSenseSensorService : BaseService() {
//
////    private inner class FaraSenseSensorServiceHandler(looper: Looper) : Handler(looper) {
////
////        override fun handleMessage(msg: Message) {
////            while (!shouldStop) {
////                try {
////                    if (BaseObservableViewModel.serviceStarted && ConnectionUtil.isDataConnectionAvailable(applicationContext)) {
////                        RestClient.getFaraSenseSensor(applicationContext, object : SuccessListener<List<FaraSenseSensor>>() {
////                            override fun onSuccess(response: List<FaraSenseSensor>) {
////                                Log.d("FARASENSE SENSOR", LOG_DSERVICE_OK)
////                                FaraSenseSensorDAO().saveFromServer(response)
////                                if (BuildConfig.DEBUG && PermissionUtil.hasPermissions(applicationContext, *PermissionUtil.PERMISSIONS_DEBUG)) {
////                                    BaseDAO().saveDataBase(applicationContext)
////                                }
////                            }
////                        }, object : ErrorListener() {
////                            override fun onError(restError: RestError) {
////                                Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
////                            }
////                        }, DateUtil.firstMomentOfTheDay, DateUtil.now)
////                        Thread.sleep(BaseService.SLEEP_WAIT.toLong())
////                    } else {
////                        Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
////                        Thread.sleep(BaseService.SLEEP_TRY_AGAIN.toLong())
////                    }
////                } catch (e: InterruptedException) {
////                    Thread.currentThread().interrupt()
////                }
////
////            }
////            stopSelf(msg.arg1)
////        }
////    }
////
////    override fun onCreate() {
////        val threadFaraSenseSensor = HandlerThread("FaraSenseSensorStartArguments", Process.THREAD_PRIORITY_BACKGROUND)
////        threadFaraSenseSensor.start()
////        looper = threadFaraSenseSensor.looper
////        handler = FaraSenseSensorServiceHandler(looper!!)
////    }
//
//    companion object {
//
////        fun download(context: Context, onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {
////            Thread {
////                RestClient.getFaraSenseSensor(context, object : SuccessListener<List<FaraSenseSensor>>() {
////                    override fun onSuccess(response: List<FaraSenseSensor>) {
////                        Log.d("FARASENSE SENSOR", LOG_DSERVICE_OK)
////                        onDownloadContentListener.onSucess()
////                        FaraSenseSensorDAO().saveFromServer(response)
////                    }
////                }, object : ErrorListener() {
////                    override fun onError(restError: RestError) {
////                        Log.d("ERROR FARASENSE SENSOR", LOG_DSERVICE_ERROR)
////                        onDownloadContentListener.onFail()
////                    }
////                }, startDate, finalDate)
////            }.start()
////        }
////    }
//}
