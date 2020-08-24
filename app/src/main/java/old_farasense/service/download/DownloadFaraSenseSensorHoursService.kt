//package old_farasense.service.download
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.Log
//import java.util.Date
//import old_farasense.api.RestClient
//import old_farasense.api.base.ErrorListener
//import old_farasense.api.base.RestError
//import old_farasense.api.base.SuccessListener
//import old_farasense.model.DAO.FaraSenseSensorHoursDAO
//import old_farasense.model.realm.FaraSenseSensorHours
//import old_farasense.service.base.BaseService
//import old_farasense.service.listener.OnDownloadContentListener
//
//@SuppressLint("Registered")
//class DownloadFaraSenseSensorHoursService : BaseService() {
//    companion object {
//
////        fun download(context: Context, onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {
////
////            Thread {
////                RestClient.getFaraSenseSensorHours(context, object : SuccessListener<List<FaraSenseSensorHours>>() {
////                    override fun onSuccess(response: List<FaraSenseSensorHours>) {
////                        Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_OK)
////                        onDownloadContentListener.onSucess()
////                        FaraSenseSensorHoursDAO().saveFromServer(response)
////
////                    }
////                }, object : ErrorListener() {
////                    override fun onError(restError: RestError) {
////                        Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_ERROR)
////                        onDownloadContentListener.onFail()
////                    }
////                }, startDate, finalDate)
////            }.start()
////        }
//    }
//
//}
