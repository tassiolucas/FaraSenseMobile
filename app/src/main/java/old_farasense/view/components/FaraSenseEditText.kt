package old_farasense.view.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText

class FaraSenseEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-Regular.ttf")
    }
}