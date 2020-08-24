package old_farasense.view.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class FaraSenseButtonBold(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    init {
        this.typeface = Typeface.createFromAsset(context.assets, "fonts/Montserrat-ExtraBold.ttf")
    }
}
