package farasense.mobile.view_model.base

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable

import farasense.mobile.service.download.DownloadFaraSenseSensorService

open class BaseObservableViewModel : BaseObservable() {

    protected var context: Context? = null

    companion object {
        fun stopServices(context: Context) {
            val intent = Intent(context, DownloadFaraSenseSensorService::class.java)
            context.startService(intent)
        }
    }
}
