package old_farasense.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.view.View
import farasense.mobile.R
import java.util.*

class BleScanCallback(private val view: View,
                      private val context: Context,
                      private val sensorUUID: UUID,
                      private val bleStatusListener: BleStatusListener) : ScanCallback() {

    private val bleDevicesGatt: ArrayList<BluetoothGatt> = arrayListOf()

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        addScanResult(result)
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        super.onBatchScanResults(results)
        for (result in results) {
            Log.i("ScanCallback", "Results: $result")
            addScanResult(result)
        }
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.e("ScanCallback", "Failed, Error Code: $errorCode")
        bleStatusListener.onError(context.getString(R.string.ble_scan_error))
    }

    private fun addScanResult(result: ScanResult) {
        // view.stopScan()
        val device = result.device
        connectDevice(device)
    }

    private fun connectDevice(device: BluetoothDevice): BluetoothGatt {
        val gattClientCallback = BleGattClientCallback(context, sensorUUID, bleStatusListener)
        val bleGatt = device.connectGatt(context, false, gattClientCallback)
        bleDevicesGatt.add(bleGatt)
        return bleGatt
    }

    fun disconnectAllDevices() {
        for (bleGatt in bleDevicesGatt) {
            bleGatt.disconnect()
        }
    }
}
