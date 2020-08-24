package old_farasense.service.listener

interface OnStartServiceDownload {
    fun onStart()
    fun onFinish()
    fun onFail()
}
