package farasense.mobile.view.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class FaraSenseEditText extends EditText {

    public FaraSenseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf"));
    }
}