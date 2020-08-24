package old_farasense.bluetooth

import android.bluetooth.*
import android.content.Context
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import farasense.mobile.R
import java.io.UnsupportedEncodingException
import java.util.*

class BleGattClientCallback(private val context: Context,
                            private val sensorUUID: UUID,
                            private val bleStatusListener: BleStatusListener) : BluetoothGattCallback() {

    private lateinit var bleGattHandler: Handler
    private val GATT_DISCONNECT_DELAY: Long = 3000

    private val BLE_LOG = "BLE MANAGER"

    private var bleConnected = false
    private var gatt: BluetoothGatt? = null

    private val bluetoothUUIDs = ArrayList<Array<ParcelUuid>>()

    init {
        bleGattHandler = Handler()
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        this.gatt = gatt

        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d(BLE_LOG, "Ble gatt failure: $status")
            disconnectGattServer()
            return
        }
        if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d(BLE_LOG, "Ble gatt sucess: $status")
            bleStatusListener.onTry()
            disconnectGattServer()
            return
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            bleConnected = true
            bleStatusListener.onConnect()
            Log.d(BLE_LOG, "Ble conectado: " + gatt.device.uuids)
            bluetoothUUIDs.add(gatt.device.uuids)
            gatt.discoverServices()
        }
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d(BLE_LOG, "Ble disconectado: $status")
            bleStatusListener.onDisconnect()
            disconnectGattServer()
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }

        val service = gatt.getService(sensorUUID)

        val listCharacteristic: MutableList<BluetoothGattCharacteristic>
        if (service != null)
            listCharacteristic = service.characteristics
        else {
            listCharacteristic = mutableListOf()
            bleStatusListener.onError(context.getString(R.string.ble_service_off))
        }

        if (listCharacteristic.isNotEmpty()) {
            for (characteristic in listCharacteristic) {
                characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                enableCharacteristicNotification(gatt, characteristic)
            }
            Log.d(BLE_LOG, "Características salvas")
        }
        else
            Log.d(BLE_LOG, "Falha nas Características")
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
            Log.d(BLE_LOG, "GattServer Disconectado.")
            bleGattHandler.postDelayed({
                bleStatusListener.onDisconnect()
            }, GATT_DISCONNECT_DELAY)
        }
    }
}
