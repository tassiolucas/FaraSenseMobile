package farasense.mobile.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import farasense.mobile.R
import farasense.mobile.view.ui.dialog.AlertGpsDialog

object GpsUtil {

    fun alertGgpDialog(activity: Activity) {
        val alertDialog = AlertGpsDialog(activity)
        alertDialog.setContentView(R.layout.adapter_alert_gps_dialog)
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun turnOnGps(activity: Activity) {
        val provider = Settings.Secure.getString(activity.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)

        if (!provider.contains("gps")) { //if gps is disabled
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(intent)
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF

        } else {
            locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            return !TextUtils.isEmpty(locationProviders)
        }
    }
}
