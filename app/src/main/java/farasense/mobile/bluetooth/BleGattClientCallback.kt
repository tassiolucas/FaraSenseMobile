package farasense.mobile.bluetooth

import android.bluetooth.*
import android.os.Build
import android.os.ParcelUuid
import android.support.annotation.RequiresApi
import android.util.Log
import java.io.UnsupportedEncodingException
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class BleGattClientCallback(private val sensorUUID: UUID,
                            private val bleStatusListener: BleStatusListener) : BluetoothGattCallback() {

    private val BLE_LOG = "BLE MANAGER"
    private val CHARACTERISTIC_UUID = "129fefae-3f58-11e9-b210-d663bd873d93"

    private var bleConnected = false
    private var gatt: BluetoothGatt? = null

    private val bluetoothUUIDs = ArrayList<Array<ParcelUuid>>()

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        this.gatt = gatt

        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d(BLE_LOG, "Ble gatt failure: $status")
            disconnectGattServer()
            return
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d(BLE_LOG, "Ble gatt sucess: $status")
            disconnectGattServer()
            return
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            bleConnected = true
            bleStatusListener.onConnect()
            Log.d(BLE_LOG, "Ble conectado: " + gatt.device.uuids)
            bluetoothUUIDs.add(gatt.device.uuids)
            gatt.discoverServices()

        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d(BLE_LOG, "Ble disconectado: $status")
            disconnectGattServer()
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }

        val service = gatt.getService(sensorUUID)

        val listCharacteristic = service.characteristics
        for (characteristic in listCharacteristic) {
            characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            enableCharacteristicNotification(gatt, characteristic)
            // characteristic.notify()
        }
        Log.d(BLE_LOG, "Características salvas")
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        super.onCharacteristicChanged(gatt, characteristic)
        val messageBytes = characteristic.value
        var messageString: String? = null

        try {
            messageString = String(messageBytes)
        } catch (e: UnsupportedEncodingException) {
            Log.e(BLE_LOG, "Incapaz de converter messagem para texto.")
        }

        // Log.d(BLE_LOG,"Mensagem: " + messageString);
        bleStatusListener.onReciveMessage(messageString!!)
    }

    private fun enableCharacteristicNotification(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val characteristicWriteSuccess = gatt.setCharacteristicNotification(characteristic, true)
        if (characteristicWriteSuccess) {
            Log.d(BLE_LOG, "Característica salva com sucesso para: " + characteristic.uuid.toString())
            enableCharacteristicConfigurationDescriptor(gatt, characteristic)
        }
    }

    private fun enableCharacteristicConfigurationDescriptor(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val descriptors = characteristic.descriptors
        for (descriptor in descriptors) {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            val descriptorWriteInitiated = gatt.writeDescriptor(descriptor)
            if (descriptorWriteInitiated) {
                Log.d(BLE_LOG, "Notificações ativadas!")
            }
        }
    }

    private fun disconnectGattServer() {
        bleConnected = false
        if (gatt != null) {
            gatt!!.disconnect()
            gatt!!.close()
        }
    }
}
