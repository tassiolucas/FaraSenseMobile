package farasense.mobile.view.components

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

class FaraSenseButtonRegular(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Regular.ttf")
    }
}
