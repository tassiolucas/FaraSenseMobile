package farasense.mobile.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleGattClientCallback extends BluetoothGattCallback {

    private final String BLE_LOG = "BLE MANAGER";
    private final String CHARACTERISTIC_UUID = "129fefae-3f58-11e9-b210-d663bd873d93";

    private boolean bleConnected = false;
    private BluetoothGatt gatt;
    private UUID sensorUUID;

    private ArrayList<ParcelUuid[]> bluetoothUUIDs = new ArrayList<>();

    public BleGattClientCallback(UUID sensorUUID) {
        this.sensorUUID = sensorUUID;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        this.gatt = gatt;

        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d(BLE_LOG, "Ble gatt failure: " + status);
            disconnectGattServer();
            return;
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d(BLE_LOG, "Ble gatt sucess: " + status);
            disconnectGattServer();
            return;
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            bleConnected = true;
            Log.d(BLE_LOG, "Ble conectado: " + gatt.getDevice().getUuids());
            bluetoothUUIDs.add(gatt.getDevice().getUuids());
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d(BLE_LOG, "Ble disconectado: " + status);
            disconnectGattServer();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return;
        }

        BluetoothGattService service = gatt.getService(sensorUUID);

        List<BluetoothGattCharacteristic> listCharacteristic = service.getCharacteristics();
        for(BluetoothGattCharacteristic characteristic : listCharacteristic) {
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            enableCharacteristicNotification(gatt, characteristic);
            characteristic.notify();
        }

        Log.d(BLE_LOG, "Características salvas");
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        byte[] messageBytes = characteristic.getValue();
        String messageString = null;

        try {
            messageString = new String(messageBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(BLE_LOG, "Incapaz de converter messagem para texto.");
        }
        Log.d(BLE_LOG,"Mensagem: " + messageString);
    }

    private void enableCharacteristicNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        boolean characteristicWriteSuccess = gatt.setCharacteristicNotification(characteristic, true);
        if (characteristicWriteSuccess) {
            Log.d(BLE_LOG, "Característica salva com sucesso para: " + characteristic.getUuid().toString());
            enableCharacteristicConfigurationDescriptor(gatt, characteristic);
        }
    }

    private void enableCharacteristicConfigurationDescriptor(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
        for (BluetoothGattDescriptor descriptor : descriptors) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            boolean descriptorWriteInitiated = gatt.writeDescriptor(descriptor);
            if (descriptorWriteInitiated) {
                Log.d(BLE_LOG,"Notificações ativadas!");
            }
        }
    }

    public void disconnectGattServer() {
        bleConnected = false;
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
        }
    }
}
