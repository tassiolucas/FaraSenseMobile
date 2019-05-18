package farasense.mobile.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log

object  BleBroadcastReceiver {
    val bleReceiver = object : BroadcastReceiver() {

        private val TAG = "BleBroadcastReceiver"

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> Log.d(TAG,"Bluetooth off")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.d(TAG,"Turning Bluetooth off...")
                    BluetoothAdapter.STATE_ON -> Log.d(TAG,"Bluetooth on")
                    BluetoothAdapter.STATE_TURNING_ON -> Log.d(TAG,"Turning Bluetooth on...")
                }
            }
        }
    }
}

