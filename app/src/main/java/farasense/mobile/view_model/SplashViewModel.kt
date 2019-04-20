package farasense.mobile.view_model

import android.app.Activity
import android.content.Context
import android.content.Intent
import farasense.mobile.service.download.DownloadFaraSenseSensorService
import farasense.mobile.view.ui.activity.DashboardActivity
import farasense.mobile.view.ui.activity.base.BaseActivity
import farasense.mobile.view_model.base.BaseObservableViewModel

class SplashViewModel(context: Context) : BaseObservableViewModel() {

    init {
        this.context = context
    }

    private fun startServices() {
        val intent = Intent(context, DownloadFaraSenseSensorService::class.java)
        context!!.startService(intent)
    }

    fun goToDashBoardActivity() {
        startServices()
        BaseActivity.startActivity((context as Activity?)!!, DashboardActivity::class.java, true)
    }
}
