package farasense.mobile.view.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import farasense.mobile.R;
import farasense.mobile.util.GpsUtil;

public class AlertGpsDialog extends Dialog {

    private Activity activity;
    private Button acceptButton;
    private Button exitButton;

    public AlertGpsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initDialog();

        acceptButton.setOnClickListener(c -> {
            GpsUtil.turnOnGps(activity);
            this.dismiss();
        });

        exitButton.setOnClickListener(c -> {
            activity.finish();
        });
    }

    private void initDialog() {
        acceptButton = findViewById(R.id.button_accept);
        exitButton = findViewById(R.id.button_exit);
    }
}
