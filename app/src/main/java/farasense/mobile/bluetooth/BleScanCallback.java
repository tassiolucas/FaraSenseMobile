package farasense.mobile.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BleScanCallback extends ScanCallback {

        private static final String FARASENSE_SENSOR_1 = "80:7D:3A:C7:22:DE";
        private static final UUID FARASENSE_CHARACTERISTIC_UUID_SENSOR_1 = UUID.fromString("129FECFC-3F58-11E9-B210-D663BD873D93");
        private static Context context;
        private HashMap<Object, Object> bleScanResults;

    public BleScanCallback(Context context, HashMap<Object, Object> bleScanResults) {
        this.context = context;
        this.bleScanResults = bleScanResults;
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
        BluetoothDevice device = result.getDevice();
        String deviceAddress = device.getAddress();
        this.bleScanResults.put(deviceAddress, device);

//        if (deviceAddress.equals(FARASENSE_SENSOR_1)) {
//            connectDevice(device);
//        }
    }

    public HashMap<Object, Object> getBleScanResults() {
        return bleScanResults;
    }

    public BluetoothGatt connectDevice(BluetoothDevice device) {
        BleGattClientCallback gattClientCallback = new BleGattClientCallback();
        BluetoothGatt bleGatt = device.connectGatt(context, false, gattClientCallback);
        return bleGatt;
    }
}
