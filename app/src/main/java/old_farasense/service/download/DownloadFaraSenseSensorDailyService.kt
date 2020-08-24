//package old_farasense.service.download
//
//import android.annotation.SuppressLint
//import android.content.Context
//import java.util.Date
//import old_farasense.api.RestClient
//import old_farasense.api.base.RestError
//import old_farasense.api.base.SuccessListener
//import old_farasense.model.DAO.FaraSenseSensorDailyDAO
//import old_farasense.model.realm.FaraSenseSensorDaily
//import old_farasense.service.base.BaseService
//import old_farasense.service.listener.OnDownloadContentListener
//
//@SuppressLint("Registered")
//class DownloadFaraSenseSensorDailyService : BaseService() {
//
//    companion object {
//
////        fun download(context: Context, onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {
////            Thread {
////                RestClient.getFaraSenseSensorDaily(context, object : SuccessListener<List<FaraSenseSensorDaily>>() {
////                    override fun onSuccess(response: List<FaraSenseSensorDaily>) {
////                        onDownloadContentListener.onSucess()
////                        FaraSenseSensorDailyDAO().saveFromServer(response)
////                    }
////                }, object : ErrorListener() {
////                    override fun onError(restError: RestError) {
////
////                    }
////                }, startDate, finalDate)
////            }.start()
////        }
//    }
//}
