package farasense.mobile.view.ui.activity.custom_view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import farasense.mobile.R
import farasense.mobile.bluetooth.BleBroadcastReceiver
import farasense.mobile.bluetooth.BleScanCallback
import farasense.mobile.bluetooth.BleStatusListener
import farasense.mobile.util.AmperSensitivityEnum
import farasense.mobile.util.Preferences
import farasense.mobile.view.components.LedStatusIndicatorView
import farasense.mobile.view.ui.activity.DashboardActivity
import farasense.mobile.view.ui.dialog.RealTimeIndicatorOptionsDialog
import io.github.dvegasa.arcpointer.ArcPointer
import java.util.*

class RealTimeCurrentIndicatorView : ConstraintLayout, BleStatusListener {

    private val zero = 0F
    private val maxValueIndicator = 1.0F

    private lateinit var bleHandlerScan: Handler
    private lateinit var bleHandlerReciveMessage: Handler
    private lateinit var bleHandlerDelay : Handler
    private lateinit var bleHandlerDelayDisconnect: Handler
    private lateinit var bleHandlerReconnect: Handler

    private lateinit var bleAnimationRunnable: Runnable

    private lateinit var sensorBleAdapter: BluetoothAdapter
    private var bleScanner: BluetoothLeScanner? = null
    private lateinit var bleSettings: ScanSettings
    private lateinit var bleFilters: MutableList<ScanFilter>
    private lateinit var bleScanResults: HashMap<Any, Any>
    private lateinit var bleScanCallback: BleScanCallback
    private lateinit var bleBluetoothLeScanner: BluetoothLeScanner

    private var bleMessage: Float = zero
    private var amperSensitivity: Float = Preferences.getInstance(context).amperSensitivity!!
    private var isReciveMessage = false
    private var bleScanning = false
    private var bleUnavailable = false

    private lateinit var indicatorCurrent: ArcPointer
    private lateinit var indicatorLabel: TextView
    private lateinit var infoStatusLabel: TextView
    private lateinit var indicatorOptionButton: ImageButton
    private lateinit var ledStatusIndicator: LedStatusIndicatorView

    // Bluetooth View
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val rootView = View.inflate(context, R.layout.real_time_indicator_view, this)

        indicatorCurrent = rootView.findViewById(R.id.indicator)
        indicatorLabel = rootView.findViewById(R.id.indicator_label)
        infoStatusLabel = rootView.findViewById(R.id.status_info_label)
        ledStatusIndicator = rootView.findViewById(R.id.led_indicator_view)
        indicatorOptionButton = rootView.findViewById(R.id.real_time_indicator_option)

        infoStatusLabel.setText(R.string.ble_initializing)

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), resources.getString(R.string.ble_not_supported), Toast.LENGTH_SHORT).show()
            bleUnavailable = true
        } else {
            val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            sensorBleAdapter = bluetoothManager.adapter
        }

        BleBroadcastReceiver.bleReceiver
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        indicatorCurrent.setNotches(5)
        indicatorCurrent.colorMarker = resources.getColor(R.color.colorAccent)
        indicatorCurrent.colorLine = resources.getColor(R.color.colorPrimaryDark)
        indicatorCurrent.setNotchesColors(resources.getColor(R.color.colorLabelCost))
        indicatorCurrent.colorBackground = resources.getColor(R.color.colorYearlyChartBar)
        indicatorCurrent.workAngle = INDICATOR_ANGLE
        indicatorCurrent.lineStrokeWidth = 3f
        indicatorCurrent.markerStrokeWidth = 6f
        indicatorCurrent.isAnimated = true
        indicatorCurrent.animationVelocity = 500L

        if (!bleUnavailable) {
            bleHandlerScan = Handler()
            bleHandlerReciveMessage = Handler()
            bleHandlerDelay = Handler()
            bleHandlerDelayDisconnect = Handler()
            bleHandlerReconnect = Handler()

            bleAnimationRunnable = Runnable {
                runOnUiThread {
                    var indicatorValue = (bleMessage / amperSensitivity)

                    if (indicatorValue < maxValueIndicator) {
                        indicatorCurrent.value = indicatorValue
                        Log.d("VAL", indicatorValue.toString())
                    } else {
                        indicatorCurrent.value = maxValueIndicator
                        Log.d("Erro de escala", indicatorValue.toString())
                    }
                    indicatorLabel.text = String.format("%.2f", bleMessage)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sensorBleAdapter.isEnabled) {
                bleScanner = sensorBleAdapter.bluetoothLeScanner

                bleSettings = ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                        .build()

                bleScanResults = HashMap()
                bleScanCallback = BleScanCallback(this, context, FARASENSE_SERVICE_UUID_SENSOR_1, this)

                this.startScan()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !sensorBleAdapter.isEnabled) {
                onError(resources.getString(R.string.ble_off))
            } else {
                onError(resources.getString(R.string.ble_not_supported))
            }
        }

        ledStatusIndicator.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sensorBleAdapter.isEnabled) {
                if (bleScanner == null)
                    bleScanner = sensorBleAdapter.bluetoothLeScanner

                if (!isReciveMessage)
                    startScan()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !sensorBleAdapter.isEnabled) {
                onError(resources.getString(R.string.ble_off))
                Toast.makeText(context!!, resources.getString(R.string.ble_off_message), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context!!, resources.getString(R.string.ble_not_supported), Toast.LENGTH_SHORT).show()
            }
        }

        indicatorOptionButton.setOnClickListener {
            val dialog = RealTimeIndicatorOptionsDialog(context as DashboardActivity, this)
            dialog.setContentView(R.layout.real_time_indicator_sensitivity_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    // Real Time Current Sensitivity
    fun setSensitivity(value: Int): Float {
        amperSensitivity = AmperSensitivityEnum.getValueFromSelection(value)
        return amperSensitivity
    }

    // Bluetooth Permission Manager
    private fun hasPermissions(): Boolean {
        if (!sensorBleAdapter.isEnabled) {
            requestBluetoothEnable()
            return false
        } else if (!hasLocationPermissions()) {
            requestLocationPermission()
            return false
        }
        return true
    }

    private fun hasLocationPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (context as DashboardActivity).requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
        }
    }

    private fun requestBluetoothEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        (context as DashboardActivity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        Log.d(TAG, "Requested user enables Bluetooth. Try starting the scan again.")

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context!!.registerReceiver(BleBroadcastReceiver.bleReceiver, filter)
    }

    // Bluetooth Scanner
    fun stopScan() {
        if (bleScanning && sensorBleAdapter.isEnabled) {
            bleHandlerDelayDisconnect.postDelayed({
                scanComplete(bleBluetoothLeScanner)
            }, SCAN_DELAY)
        }
        Log.d(TAG, "Stop scan.")
        bleScanning = false
    }

    private fun startScan() {
        Log.d(TAG, "Start Scan!!!")

        if (!hasPermissions() || bleScanning) {
            return
        }

        bleHandlerDelay.postDelayed({
            bleFilters = ArrayList()
            val bleScanFilter = ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(FARASENSE_SERVICE_UUID_SENSOR_1))
                    .build()
            bleFilters.add(bleScanFilter)
            bleScanner?.startScan(bleFilters, bleSettings, bleScanCallback)
            bleHandlerScan.postDelayed({
                this.stopScan()
            }, SCAN_PERIOD)
            bleBluetoothLeScanner = sensorBleAdapter.bluetoothLeScanner
            bleScanning = true
        }, SCAN_DELAY)

        onTry()
    }

    private fun scanComplete(bleBluetoothLeScanner: BluetoothLeScanner) {
        if (bleScanResults.isEmpty() && !isReciveMessage) {
            Log.d(TAG, "Found nothing.")
            onError(resources.getString(R.string.ble_scan_error))
            bleHandlerDelayDisconnect.postDelayed({
                onDisconnect()
            }, SCAN_DELAY)
        }
        for (deviceAddress in bleScanResults.keys) {
            Log.d(TAG, "Found device: $deviceAddress")
        }

        bleBluetoothLeScanner.stopScan(bleScanCallback)
    }

    // Bluetooth Listener
    override fun onReciveMessage(message: String) {
        if (isReciveMessage) {
            bleHandlerReciveMessage.postDelayed(bleAnimationRunnable, UPDATE_INDICATOR_SPEED.toLong())
        }
        bleMessage = java.lang.Float.parseFloat(message)
    }

    override fun onTry() {
        runOnUiThread {
            infoStatusLabel.setText(R.string.ble_try)
        }
        ledStatusIndicator.setStatus(LedStatusIndicatorView.TRY)
    }

    override fun onConnect() {
        isReciveMessage = true
        runOnUiThread {
            infoStatusLabel.setText(R.string.ble_connected)
        }
        ledStatusIndicator.setStatus(LedStatusIndicatorView.CONNECT)
    }

    override fun onDisconnect() {
        isReciveMessage = false
        runOnUiThread {
            infoStatusLabel.setText(R.string.ble_disconnected)
            indicatorCurrent.value = zero
            indicatorLabel.text = String.format("%.1f", zero)
        }
        ledStatusIndicator.setStatus(LedStatusIndicatorView.DISCONNECT)
    }

    override fun onError(error : String) {
        isReciveMessage = false
        runOnUiThread {
            if (error.isEmpty())
                infoStatusLabel.setText(R.string.ble_off)
            else
                infoStatusLabel.text = error
        }
        ledStatusIndicator.setStatus(LedStatusIndicatorView.ERROR)
    }

    // Bluetooth Object
    companion object {
        private val REQUEST_ENABLE_BT = 1
        private val REQUEST_FINE_LOCATION = 0
        private val SCAN_PERIOD: Long = 3000
        private val SCAN_DELAY: Long = 3500
        private val UPDATE_INDICATOR_SPEED = 500
        private val INDICATOR_ANGLE = 160

        private val TAG = RealTimeCurrentIndicatorView::class.java.toString()
        private val FARASENSE_SERVICE_UUID_SENSOR_1 = UUID.fromString("129fecfc-3f58-11e9-b210-d663bd873d93")
    }
}
