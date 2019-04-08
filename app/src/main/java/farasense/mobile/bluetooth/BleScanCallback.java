package farasense.mobile.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import farasense.mobile.view.ui.activity.fragment.RealTimeCurrentIndicatorView;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BleScanCallback extends ScanCallback {

        private static Context context;
        private RealTimeCurrentIndicatorView view;
        private UUID sensorUUID;
        private ArrayList<BluetoothGatt> bleDevicesGatt;

    public BleScanCallback(RealTimeCurrentIndicatorView realTimeCurrentIndicatorView, Context context, UUID sensorUUID) {
        this.view = realTimeCurrentIndicatorView;
        this.context = context;
        this.sensorUUID = sensorUUID;
        bleDevicesGatt = new ArrayList<>();
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        addScanResult(result);
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
        for (ScanResult result : results) {
            Log.i("ScanCallback", "Results: "+ result.toString());
            addScanResult(result);
        }
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
        Log.e("ScanCallback", "Failed, Error Code: " + errorCode);
    }

    private void addScanResult(ScanResult result) {
        view.stopScan();
        BluetoothDevice device = result.getDevice();
        connectDevice(device);
    }

    public BluetoothGatt connectDevice(BluetoothDevice device) {
        BleGattClientCallback gattClientCallback = new BleGattClientCallback(sensorUUID);
        BluetoothGatt bleGatt = device.connectGatt(context, false, gattClientCallback);
        bleDevicesGatt.add(bleGatt);
        return bleGatt;
    }

    public void disconnectAllDevices(){
        for(BluetoothGatt bleGatt : bleDevicesGatt) {
            bleGatt.disconnect();
        }
    }
}
