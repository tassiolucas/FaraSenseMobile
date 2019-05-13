package farasense.mobile.view_model.base

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable

import farasense.mobile.service.download.DownloadFaraSenseSensorService

open class BaseObservableViewModel : BaseObservable() {

    protected var context: Context? = null

    companion object {
        var serviceStarted = false
    }

    fun startServices(context: Context) {
        if (!serviceStarted) {
            val intent = Intent(context, DownloadFaraSenseSensorService::class.java)
            context.startService(intent)
            serviceStarted = true
        }
    }

    fun stopServices(context: Context) {
        if (serviceStarted) {
            val intent = Intent(context, DownloadFaraSenseSensorService::class.java)
            context.stopService(intent)
            serviceStarted = false
        }
    }
}
