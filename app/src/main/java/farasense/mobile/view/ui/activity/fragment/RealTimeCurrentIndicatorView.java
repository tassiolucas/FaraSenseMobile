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
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.shinelw.library.ColorArcProgressBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import farasense.mobile.R;
import farasense.mobile.bluetooth.BleScanCallback;
import farasense.mobile.view.ui.activity.DashboardActivity;

import static android.content.Context.BLUETOOTH_SERVICE;

public class RealTimeCurrentIndicatorView extends ConstraintLayout {

    private final static String BLE_NOT_SUPPORTED_MESSAGE = "Bluetooth Low Energy não suportado.";
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_FINE_LOCATION = 0;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = RealTimeCurrentIndicatorView.class.toString();
    private static final UUID FARASENSE_SERVICE_UUID_SENSOR_1 = UUID.fromString("129fecfc-3f58-11e9-b210-d663bd873d93");
    private static final UUID RANDOM = UUID.randomUUID();

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

    private View rootView;
    private ColorArcProgressBar indicatorProgressBar;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init(Context context) {
        this.context = context;
        rootView = View.inflate(context, R.layout.real_time_current_indicator_view, this);

        indicatorProgressBar = rootView.findViewById(R.id.indicator);

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
        bleHandler = new Handler();
        if (Build.VERSION.SDK_INT >= 21) {
            bleScanner = sensorBleAdapter.getBluetoothLeScanner();

            bleSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();

            bleScanResults = new HashMap<>();
            bleScanCallback = new BleScanCallback(this, context, FARASENSE_SERVICE_UUID_SENSOR_1);

            bleHandler.postDelayed(this::startScan, SCAN_PERIOD);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void disconnectAllDevices() {
        bleScanCallback.disconnectAllDevices();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startScan() {
            Log.d(TAG, "Start Scan!!!");
            if (!hasPermissions() || bleScanning) {
                return;
            }
            bleFilters = new ArrayList<>();
            ScanFilter bleScanFilter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(FARASENSE_SERVICE_UUID_SENSOR_1))
                    .build();
            bleFilters.add(bleScanFilter);
            bleScanner.startScan(bleFilters, bleSettings, bleScanCallback);

            bleBluetoothLeScanner = sensorBleAdapter.getBluetoothLeScanner();
            bleScanning = true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void switchScan(final boolean enable) {
        if (enable) {
            if (!hasPermissions() || bleScanning) {
                return;
            }
            bleFilters = new ArrayList<>();
            ScanFilter bleScanFilter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(FARASENSE_SERVICE_UUID_SENSOR_1))
                    .build();
            bleFilters.add(bleScanFilter);
            bleScanner.startScan(bleFilters, bleSettings, bleScanCallback);
            bleHandler.postDelayed(this::stopScan, SCAN_PERIOD);
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
    public void stopScan() {
        if (bleScanning && sensorBleAdapter != null && sensorBleAdapter.isEnabled()) {
            if (scanComplete()) {
                bleBluetoothLeScanner.stopScan(bleScanCallback);
            }
        }

        bleScanCallback = null;
        bleScanning = false;
        bleHandler = null;
    }

    private boolean scanComplete() {
        if (bleScanResults.isEmpty()) {
            return false;
        }
        for (Object deviceAddress : bleScanResults.keySet()) {
            Log.d(TAG, "Found device: " + deviceAddress.toString());
        }
        return true;
    }
}
