package farasense.mobile.service.download

import android.annotation.SuppressLint
import android.content.Context
import java.util.Date
import farasense.mobile.api.RestClient
import farasense.mobile.api.base.ErrorListener
import farasense.mobile.api.base.RestError
import farasense.mobile.api.base.SuccessListener
import farasense.mobile.model.DAO.FaraSenseSensorDailyDAO
import farasense.mobile.model.realm.FaraSenseSensorDaily
import farasense.mobile.service.base.BaseService
import farasense.mobile.service.listener.OnDownloadContentListener

@SuppressLint("Registered")
class DownloadFaraSenseSensorDailyService : BaseService() {

    companion object {

        fun download(context: Context, onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {
            Thread {
                RestClient.getFaraSenseSensorDaily(context, object : SuccessListener<List<FaraSenseSensorDaily>>() {
                    override fun onSuccess(response: List<FaraSenseSensorDaily>) {
                        onDownloadContentListener.onSucess()
                        FaraSenseSensorDailyDAO().saveFromServer(response)
                    }
                }, object : ErrorListener() {
                    override fun onError(restError: RestError) {

                    }
                }, startDate, finalDate)
            }.start()
        }
    }
}
