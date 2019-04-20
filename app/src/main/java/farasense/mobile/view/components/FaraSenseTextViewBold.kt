package farasense.mobile.view.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class FaraSenseTextViewBold(context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatTextView(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Bold.ttf")
    }
}