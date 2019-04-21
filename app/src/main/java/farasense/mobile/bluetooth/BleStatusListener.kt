package farasense.mobile.bluetooth

interface BleStatusListener {
    fun onReciveMessage(message: String)
    fun onConnect()
}
