package farasense.mobile.bluetooth;

public interface BleStatusListener {
    void onReciveMessage(String message);
    void onConnect();
}
