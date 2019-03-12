package farasense.mobile.view.ui.activity.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
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
import java.util.List;
import javax.annotation.Nullable;

public class RealTimeCurrentIndicatorView extends View {

    private final static String BLE_NOT_SUPPORTED_MESSAGE = "Bluetooth Low Energy não suportado.";
    private final static int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;

    private Context context;
    private Handler bleHandler;
    private BluetoothAdapter sensorBleAdapter;
    private BluetoothLeScanner bleScanner;
    private ScanSettings bleSettings;
    private List<ScanFilter> bleFilters;

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

        bleHandler = new Handler();

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), BLE_NOT_SUPPORTED_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        sensorBleAdapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sensorBleAdapter == null || !sensorBleAdapter.isEnabled()) {
            Intent enableBleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                bleScanner = sensorBleAdapter.getBluetoothLeScanner();
                bleSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                scanLeDevice(true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            bleHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        sensorBleAdapter.stopLeScan(bleScanCallback);
                    } else {
                        bleScanner.stopScan(scanCallback);
                    }
                }
            }, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT < 21) {
               sensorBleAdapter.startLeScan(bleScanCallback);
            } else {
                bleScanner.startScan(bleFilters, bleSettings, scanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                sensorBleAdapter.stopLeScan(bleScanCallback);
            } else {
                bleScanner.stopScan(scanCallback);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i("ScanCallback", "Callback Type: " + String.valueOf(callbackType));
            Log.i("ScanCallback", "Result: " + result.toString());
            BluetoothDevice btDevice = result.getDevice();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for (ScanResult result : results) {
                Log.i("ScanCallback", "Results: "+ result.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("ScanCallback", "Failed, Error Code: " + errorCode);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("BleScanCallback", "OnLeScan: " + device.toString());
        }
    };

}
