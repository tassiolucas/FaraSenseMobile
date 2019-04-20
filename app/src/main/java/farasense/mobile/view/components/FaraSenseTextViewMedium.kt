package farasense.mobile.view.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class FaraSenseTextViewMedium(context: Context, attrs: AttributeSet) : TextView(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Medium.ttf")
    }
}
