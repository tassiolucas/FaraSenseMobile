package farasense.mobile.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleGattClientCallback extends BluetoothGattCallback {

    private boolean bleConnected = false;
    private BluetoothGatt gatt;

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        this.gatt = gatt;

        if (status == BluetoothGatt.GATT_FAILURE) {
            disconnectGattServer();
            return;
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
            disconnectGattServer();
            return;
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            bleConnected = true;
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            disconnectGattServer();
        }
    }

    private void disconnectGattServer() {
        bleConnected = false;
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
        }
    }

}
