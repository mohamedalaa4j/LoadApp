package com.mohamedalaa4j.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    // Paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Colors
    private var buttonBackgroundColor = context.getColor(R.color.background)
    private var buttonFillingColor = context.getColor(R.color.purple_700)
    private var textColor = context.getColor(R.color.white)

    init {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun drawBackground(canvas: Canvas) {

        // Draw empty button
        paint.color = buttonBackgroundColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), 20f, 20f, paint)

        // Draw the filling button progress
        paint.color = buttonFillingColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(0f, 0f, (widthSize * 0.25).toFloat(), heightSize.toFloat(), 20f, 20f, paint)

        paint.color = textColor
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(context.getString(R.string.download), (widthSize / 3).toFloat(), (heightSize / 2).toFloat(), paint)

    }

}

sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}