package farasense.mobile.view.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import farasense.mobile.R

class LedStatusIndicatorView : View {

    private var ledIndicator: Paint? = null
    private var ledCircularIndicator: Paint? = null
    private var statusLed = -1

    private var layoutCx = 0.0F
    private var layoutCy = 0.0F
    private var radius = 0.0F

    private var ledStop = false
    private var ledTurnFade = false
    private var ledIndicatorAlpha = 0

    companion object {
        const val CONNECT = 1
        const val DISCONNECT = 0
        const val TRY = 2
        const val ERROR = 3
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        ledIndicator = Paint()
        ledCircularIndicator = Paint()

        ledIndicator!!.style = Paint.Style.FILL_AND_STROKE
        ledCircularIndicator!!.style = Paint.Style.STROKE
        ledCircularIndicator!!.strokeWidth = 3F

        ledIndicator!!.color = ContextCompat.getColor(context, R.color.ledGray)
        ledCircularIndicator!!.color = ContextCompat.getColor(context, R.color.ledGrayDark)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        layoutCx = (height.toFloat() / 2)
        layoutCy = (width.toFloat() / 2)
        radius = (height.toFloat())
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (ledIndicator != null && ledCircularIndicator != null) {
            setLedStatusColor(statusLed, ledIndicator!!, ledCircularIndicator!!)

            blinkLedIndicator()

            drawIt(ledIndicator!!, ledCircularIndicator!!, canvas!!)
        }
    }

    private fun setLedStatusColor(statusLed : Int, ledIndicator: Paint, ledCircularIndicator: Paint) {
        when (statusLed) {
            0 -> {
                ledStop = true
                ledIndicator.color = ContextCompat.getColor(context, R.color.ledRed)
                ledCircularIndicator.color = ContextCompat.getColor(context, R.color.ledRedDark)
            }
            1 -> {
                ledStop = false
                ledIndicator.color = ContextCompat.getColor(context, R.color.ledGreen)
                ledCircularIndicator.color = ContextCompat.getColor(context, R.color.ledGreenDark)
            }
            2 -> {
                ledStop = false
                ledIndicator.color = ContextCompat.getColor(context, R.color.ledYellow)
                ledCircularIndicator.color = ContextCompat.getColor(context, R.color.ledYellowDark)
            }
            3 -> {
                ledStop = true
                ledIndicator.color = ContextCompat.getColor(context, R.color.ledGray)
                ledCircularIndicator.color = ContextCompat.getColor(context, R.color.ledGrayDark)
            }
            else -> {
                ledStop = true
                ledIndicator.color = ContextCompat.getColor(context, R.color.ledGray)
                ledCircularIndicator.color = ContextCompat.getColor(context, R.color.ledGrayDark)
            }
        }
    }

    private fun blinkLedIndicator() {
        if (!ledStop) {
            if (!ledTurnFade) {
                ledIndicatorAlpha += 3
                ledIndicator!!.alpha = if (ledIndicatorAlpha < 255) ledIndicatorAlpha else 255
                if (ledIndicatorAlpha > 255) {
                    ledTurnFade = true
                }
            } else {
                ledIndicatorAlpha -= 3
                ledIndicator!!.alpha = if (ledIndicatorAlpha > 0) ledIndicatorAlpha else 0
                if (ledIndicatorAlpha <= 0) {
                    ledTurnFade = false
                }
            }
            ThreadUtils.runOnUiThread {
                invalidate()
            }
        }
    }

    private fun drawIt(ledIndicator: Paint, ledCircularIndicator: Paint, canvas: Canvas?) {
        canvas?.drawCircle(layoutCx, layoutCy, radius / 4.5F, ledIndicator)
        canvas?.drawCircle(layoutCx, layoutCy, radius / 2.5F, ledCircularIndicator)
    }

    fun setStatus(status: Int) {
        this.statusLed = status
        ThreadUtils.runOnUiThread {
            invalidate()
        }
    }
}