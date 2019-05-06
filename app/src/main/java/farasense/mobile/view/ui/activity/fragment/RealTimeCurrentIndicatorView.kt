package farasense.mobile.view.ui.activity.fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID
import farasense.mobile.R
import farasense.mobile.bluetooth.BleScanCallback
import farasense.mobile.bluetooth.BleStatusListener
import farasense.mobile.view.ui.activity.DashboardActivity
import io.github.dvegasa.arcpointer.ArcPointer
import android.content.Context.BLUETOOTH_SERVICE

class RealTimeCurrentIndicatorView : ConstraintLayout, BleStatusListener {

    private lateinit var bleHandler: Handler
    private lateinit var bleHandler2: Handler
    private lateinit var sensorBleAdapter: BluetoothAdapter
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var bleSettings: ScanSettings
    private lateinit var bleFilters: MutableList<ScanFilter>
    private lateinit var bleScanResults: HashMap<Any, Any>
    private lateinit var bleScanCallback: BleScanCallback
    private lateinit var bleBluetoothLeScanner: BluetoothLeScanner
    private var bleMessage: Float = 0F
    private var isReciveMessage = false
    private var bleScanning = false
    private var bleUnavailable = false

    private lateinit var bleAnimationRunnable: Runnable
    private lateinit var indicatorCurrent: ArcPointer
    private lateinit var indicatorLabel: TextView

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    constructor(context: Context) : super(context) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun init(context: Context) {

        val rootView = View.inflate(context, R.layout.real_time_current_indicator_view, this)

        indicatorCurrent = rootView.findViewById(R.id.indicator)
        indicatorLabel = rootView.findViewById(R.id.indicator_label)

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), BLE_NOT_SUPPORTED_MESSAGE, Toast.LENGTH_SHORT).show()
            bleUnavailable = true
        } else {
            val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            sensorBleAdapter = bluetoothManager.adapter
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
            bleHandler = Handler()
            bleHandler2 = Handler()

            bleAnimationRunnable = Runnable {
                indicatorCurrent.value = bleMessage * 0.1f
                indicatorLabel.text = String.format("%.2f", bleMessage)
            }

            if (Build.VERSION.SDK_INT >= 21) {
                bleScanner = sensorBleAdapter.bluetoothLeScanner

                bleSettings = ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                        .build()

                bleScanResults = HashMap()
                bleScanCallback = BleScanCallback(this, context, FARASENSE_SERVICE_UUID_SENSOR_1, this)

                bleHandler.postDelayed({ this.startScan() }, SCAN_PERIOD)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun disconnectAllDevices() {
        bleScanCallback.disconnectAllDevices()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun startScan() {
        Log.d(TAG, "Start Scan!!!")
        if (!hasPermissions() || bleScanning) {
            return
        }
        bleFilters = ArrayList()
        val bleScanFilter = ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(FARASENSE_SERVICE_UUID_SENSOR_1))
                .build()
        bleFilters.add(bleScanFilter)
        bleScanner.startScan(bleFilters, bleSettings, bleScanCallback)

        bleBluetoothLeScanner = sensorBleAdapter.bluetoothLeScanner
        bleScanning = true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun switchScan(enable: Boolean) {
        if (enable) {
            if (!hasPermissions() || bleScanning) {
                return
            }
            bleFilters = ArrayList()
            val bleScanFilter = ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(FARASENSE_SERVICE_UUID_SENSOR_1))
                    .build()
            bleFilters.add(bleScanFilter)
            bleScanner.startScan(bleFilters, bleSettings, bleScanCallback)
            bleHandler.postDelayed({ this.stopScan() }, SCAN_PERIOD)
            bleBluetoothLeScanner = sensorBleAdapter.bluetoothLeScanner
            bleScanning = true
        } else {
            bleScanner.stopScan(bleScanCallback)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun hasLocationPermissions(): Boolean {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        (context as DashboardActivity).requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
    }

    private fun requestBluetoothEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        (context as DashboardActivity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        Log.d(TAG, "Requested user enables Bluetooth. Try starting the scan again.")
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun stopScan() {
        if (bleScanning && sensorBleAdapter.isEnabled) {
            if (scanComplete()) {
                bleBluetoothLeScanner.stopScan(bleScanCallback)
            }
        }

        bleScanning = false
    }

    private fun scanComplete(): Boolean {
        if (bleScanResults.isEmpty()) {
            return false
        }
        for (deviceAddress in bleScanResults.keys) {
            Log.d(TAG, "Found device: $deviceAddress")
        }
        return true
    }

    // BLE Manager Interface
    override fun onReciveMessage(message: String) {
        if (isReciveMessage) {
            bleHandler2.postDelayed(bleAnimationRunnable, UPDATE_INDICATOR_SPEED.toLong())
        }
        bleMessage = java.lang.Float.parseFloat(message)
    }

    override fun onConnect() {
        isReciveMessage = true
    }

    companion object {

        private val BLE_NOT_SUPPORTED_MESSAGE = "Bluetooth Low Energy não suportado."
        private val REQUEST_ENABLE_BT = 1
        private val REQUEST_FINE_LOCATION = 0
        private val SCAN_PERIOD: Long = 10000
        private val UPDATE_INDICATOR_SPEED = 500
        private val INDICATOR_ANGLE = 160

        private val TAG = RealTimeCurrentIndicatorView::class.java.toString()
        private val FARASENSE_SERVICE_UUID_SENSOR_1 = UUID.fromString("129fecfc-3f58-11e9-b210-d663bd873d93")
    }
}
