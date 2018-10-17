package farasense.mobile.view.components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class FaraSenseWrappingViewPager extends ViewPager {

    private int height;

    public FaraSenseWrappingViewPager(Context context) {
        super(context);
        height = 0;
    }

    public FaraSenseWrappingViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        height = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int children = getChildCount();
        for(int i = 0; i < children; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) {
                height = h;
            }
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}