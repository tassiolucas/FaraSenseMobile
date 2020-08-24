//package old_farasense.dialog
//
//import android.app.Activity
//import android.app.Dialog
//import android.widget.Button
//
//import farasense.mobile.R
//import old_farasense.util.GpsUtil
//
//class AlertGpsDialog(private val activity: Activity) : Dialog(activity) {
//    private var acceptButton: Button? = null
//    private var exitButton: Button? = null
//
//    override fun onStart() {
//        super.onStart()
//
//        initDialog()
//
//        acceptButton!!.setOnClickListener { c ->
//            GpsUtil.turnOnGps(activity)
//            this.dismiss()
//        }
//
//        exitButton!!.setOnClickListener { c -> activity.finish() }
//    }
//
//    private fun initDialog() {
//        acceptButton = findViewById(R.id.button_accept)
//        exitButton = findViewById(R.id.button_exit)
//    }
//}
