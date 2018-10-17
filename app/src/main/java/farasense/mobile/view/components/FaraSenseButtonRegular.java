package farasense.mobile.view.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class FaraSenseButtonRegular extends AppCompatButton {

    public FaraSenseButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf"));
    }
}
