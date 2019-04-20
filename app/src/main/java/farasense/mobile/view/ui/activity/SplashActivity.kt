package farasense.mobile.view.ui.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import butterknife.ButterKnife
import farasense.mobile.R
import farasense.mobile.databinding.SplashDataBinding
import farasense.mobile.service.base.BaseService
import farasense.mobile.service.listener.OnStartServiceDownload
import farasense.mobile.util.ConnectionUtil
import farasense.mobile.util.PermissionUtil
import farasense.mobile.view.ui.activity.base.BaseActivity
import farasense.mobile.view_model.SplashViewModel
import farasense.mobile.view_model.base.BaseObservableViewModel

class SplashActivity : BaseActivity() {
    private var binding: SplashDataBinding? = null
    private var viewModel: SplashViewModel? = null
    private var shouldStopService = true

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ButterKnife.bind(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            TAG_RESULT_PERMISSION_OK -> {
                if (grantResults.size > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initScreen()
                } else {
                    finish()
                }
                return
            }
        }
    }

    @SuppressLint("ShowToast")
    fun initScreen() {
        viewModel = SplashViewModel(this)
        binding!!.splash = viewModel

        if (ConnectionUtil.isDataConnectionAvailable(applicationContext)) {
            downloadContent()
            // Para agilizar a programação...
            // TODO: Remover após desenvolvimento
            // viewModel.goToDashBoardActivity();
        } else {
            Toast.makeText(this, R.string.error_no_connection_message, Toast.LENGTH_SHORT)
            viewModel!!.goToDashBoardActivity()
        }
    }

    private fun downloadContent() {
        BaseService.initialDownloadDataSensors(object : OnStartServiceDownload {
            @SuppressLint("ShowToast")
            override fun onStart() {
                Toast.makeText(applicationContext, R.string.loading, Toast.LENGTH_SHORT)
            }

            override fun onFinish() {
                runOnUiThread { viewModel!!.goToDashBoardActivity() }
                shouldStopService = false
            }

            @SuppressLint("ShowToast")
            override fun onFail() {
                Toast.makeText(applicationContext, R.string.error_downloading_message, Toast.LENGTH_SHORT)
            }
        })
    }

    public override fun onResume() {
        super.onResume()

        if (!PermissionUtil.hasPermissions(this, *PermissionUtil.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSIONS, PermissionUtil.PERMISSION_ALL)
        } else {
            initScreen()
        }
    }

    override fun onBackPressed() {}

    public override fun onDestroy() {
        super.onDestroy()
        if (shouldStopService) {
            BaseObservableViewModel.stopServices(this)
        }
    }

    companion object {
        private val TAG_RESULT_PERMISSION_OK = 1
    }
}
