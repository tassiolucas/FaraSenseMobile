package old_farasense.view.components

import android.content.Context

import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class FaraSenseWrappingViewPager : ViewPager {
    private var viewHeight: Int = 0

    constructor(context: Context) : super(context) {
        viewHeight = 0
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        viewHeight = 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec

        val children = childCount
        for (i in 0 until children) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > viewHeight) {
                viewHeight = h
            }
        }

        if (viewHeight != 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(viewHeight, View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}