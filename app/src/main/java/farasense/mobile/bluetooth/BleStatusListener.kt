package farasense.mobile.bluetooth

interface BleStatusListener {
    fun onTry()
    fun onReciveMessage(message: String)
    fun onConnect()
    fun onDisconnect()
    fun onError()
}
