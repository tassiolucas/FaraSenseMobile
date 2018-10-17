package farasense.mobile.view.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

public class FaraSenseButtonBold extends AppCompatButton {

    public FaraSenseButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-ExtraBold.ttf"));
    }
}
