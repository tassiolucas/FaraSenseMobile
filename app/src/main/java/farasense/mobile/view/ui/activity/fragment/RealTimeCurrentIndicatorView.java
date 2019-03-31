package farasense.mobile.view.ui.activity.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import farasense.mobile.bluetooth.BleScanCallback;
import farasense.mobile.view.ui.activity.DashboardActivity;

import static android.content.Context.BLUETOOTH_SERVICE;

public class RealTimeCurrentIndicatorView extends View {

    private final static String BLE_NOT_SUPPORTED_MESSAGE = "Bluetooth Low Energy não suportado.";
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_FINE_LOCATION = 0;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = RealTimeCurrentIndicatorView.class.toString();

    private Context context;
    private Handler bleHandler;
    private BluetoothAdapter sensorBleAdapter;
    private BluetoothLeScanner bleScanner;
    private BluetoothGattCharacteristic bleCharacteristic;
    private ScanSettings bleSettings;
    private List<ScanFilter> bleFilters;
    private ArrayList<BluetoothDevice> devicesList;
    private HashMap<Object, Object> bleScanResults;
    private BleScanCallback bleScanCallback;
    private BluetoothLeScanner bleBluetoothLeScanner;

    private boolean bleDeviceConnected = false;
    private boolean bleScanning = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RealTimeCurrentIndicatorView(Context context) {
        super(context);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RealTimeCurrentIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RealTimeCurrentIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RealTimeCurrentIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init(Context context) {
        this.context = context;

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, BLE_NOT_SUPPORTED_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
        sensorBleAdapter = bluetoothManager.getAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            bleScanner = sensorBleAdapter.getBluetoothLeScanner();

            bleSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();

            bleScanResults = new HashMap<>();
            bleScanCallback = new BleScanCallback(bleScanResults);

            startScan(true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startScan(final boolean enable) {
        if (enable) {
            if (!hasPermissions() || bleScanning) {
                return;
            }
            bleHandler = new Handler();
            bleHandler.postDelayed(this::stopScan, SCAN_PERIOD);
            bleScanner.startScan(bleFilters, bleSettings, bleScanCallback);
            bleBluetoothLeScanner = sensorBleAdapter.getBluetoothLeScanner();
            bleScanning = true;
        } else {
            bleScanner.stopScan(bleScanCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissions() {
        if (sensorBleAdapter == null || !sensorBleAdapter.isEnabled()) {
            requestBluetoothEnable();
            return false;
        } else if (!hasLocationPermissions()) {
            requestLocationPermission();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasLocationPermissions() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        ((DashboardActivity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    private void requestBluetoothEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((DashboardActivity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        Log.d(TAG, "Requested user enables Bluetooth. Try starting the scan again.");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScan() {
        if (bleScanning && sensorBleAdapter != null && sensorBleAdapter.isEnabled()) {
            bleBluetoothLeScanner.stopScan(bleScanCallback);
            scanComplete();
        }

        bleScanCallback = null;
        bleScanning = false;
        bleHandler = null;
    }

    private void scanComplete() {
        if (bleScanResults.isEmpty()) {
            return;
        }
        for (Object deviceAddress : bleScanResults.keySet()) {
            Log.d(TAG, "Found device: " + deviceAddress.toString());
        }
    }
}
