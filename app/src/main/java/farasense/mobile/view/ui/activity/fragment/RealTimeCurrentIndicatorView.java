package farasense.mobile.view.ui.activity.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import farasense.mobile.view.ui.activity.DashboardActivity;

public class RealTimeCurrentIndicatorView extends View {

    private final static String BLE_NOT_SUPPORTED_MESSAGE = "Bluetooth Low Energy não suportado.";
    private final static int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;

    private Context context;
    private Handler bleHandler;
    private BluetoothAdapter sensorBleAdapter;
    private BluetoothLeScanner bleScanner;
    private BluetoothGattCharacteristic bleCharacteristic;
    private ScanSettings bleSettings;
    private List<ScanFilter> bleFilters;
    private ArrayList<BluetoothDevice> devicesList;

    private static final String FARASENSE_SENSOR_1 = "80:7D:3A:C7:22:DE";
    private static final UUID FARASENSE_CHARACTERISTIC_UUID_SENSOR_1 = UUID.fromString("129FECFC-3F58-11E9-B210-D663BD873D93");

    private boolean bleDeviceConnected = false;
    private boolean bleScanDevice = false;

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

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, BLE_NOT_SUPPORTED_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        sensorBleAdapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sensorBleAdapter == null || !sensorBleAdapter.isEnabled()) {
            Intent enableBleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((DashboardActivity) context).startActivityForResult(enableBleIntent, REQUEST_ENABLE_BT);
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
//            bleHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (Build.VERSION.SDK_INT < 21) {
//                        sensorBleAdapter.stopLeScan(bleScanCallback);
//                    } else {
//                        bleScanner.stopScan(scanCallback);
//                    }
//                }
//            }, SCAN_PERIOD);

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

    private BluetoothGattCallback bleCalback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer();
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer();
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // mConnected = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer();
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                devicesList.add(gatt.getDevice());
            }
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                devicesList.remove(gatt.getDevice());
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            BluetoothGattService bleService = gatt.getService(FARASENSE_CHARACTERISTIC_UUID_SENSOR_1);
            bleCharacteristic = bleService.getCharacteristic(FARASENSE_CHARACTERISTIC_UUID_SENSOR_1);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };

    public void disconnectGattServer() {
//        mConnected = false;
//        if (mGatt != null) {
//            mGatt.disconnect();
//            mGatt.close();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            BluetoothDevice btDevice = result.getDevice();

            if (btDevice != null)
                Log.i("BLE DEVICE: ", btDevice.toString());

            if (btDevice.toString().equals(FARASENSE_SENSOR_1) && !bleDeviceConnected) {
                btDevice.connectGatt(context, true, bleCalback);
                bleScanner.stopScan(scanCallback);
                bleDeviceConnected = true;
            }
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
