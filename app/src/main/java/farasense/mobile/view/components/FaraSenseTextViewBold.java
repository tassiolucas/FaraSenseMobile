package farasense.mobile.view.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FaraSenseTextViewBold extends android.support.v7.widget.AppCompatTextView {

    public FaraSenseTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Bold.ttf"));
    }
}