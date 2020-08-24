package old_farasense.view.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class FaraSenseTextViewRegular(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Regular.ttf")
    }
}