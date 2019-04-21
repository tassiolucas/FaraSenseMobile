package farasense.mobile.service.download

import android.annotation.SuppressLint
import android.util.Log
import java.util.Date
import farasense.mobile.api.RestClient
import farasense.mobile.api.base.ErrorListener
import farasense.mobile.api.base.RestError
import farasense.mobile.api.base.SuccessListener
import farasense.mobile.model.DAO.FaraSenseSensorHoursDAO
import farasense.mobile.model.realm.FaraSenseSensorHours
import farasense.mobile.service.base.BaseService
import farasense.mobile.service.listener.OnDownloadContentListener

@SuppressLint("Registered")
class DownloadFaraSenseSensorHoursService : BaseService() {
    companion object {

        fun download(onDownloadContentListener: OnDownloadContentListener, startDate: Date, finalDate: Date) {

            Thread {
                RestClient.getInstance().getFaraSenseSensorHours(object : SuccessListener<List<FaraSenseSensorHours>>() {
                    override fun onSuccess(response: List<FaraSenseSensorHours>) {
                        Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_OK)
                        onDownloadContentListener.onSucess()
                        FaraSenseSensorHoursDAO().saveFromServer(response)

                    }
                }, object : ErrorListener() {
                    override fun onError(restError: RestError) {
                        Log.d("ERROR FARASENSE HOURS", LOG_DSERVICE_ERROR)
                        onDownloadContentListener.onFail()
                    }
                }, startDate, finalDate)
            }.start()
        }
    }

}
