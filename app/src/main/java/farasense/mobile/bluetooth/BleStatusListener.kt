package farasense.mobile.bluetooth

interface BleStatusListener {
    fun onReciveMessage(message: String)
    fun onTry()
    fun onConnect()
    fun onDisconnect()
    fun onError(error: String)
}
