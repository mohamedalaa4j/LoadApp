package com.mohamedalaa4j.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    var progressPercentage = 0.0

    private var labelText = context.getString(R.string.download)

    private val valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
            }

            ButtonState.Loading -> {
                labelText = context.getString(R.string.downloading)
                invalidate()
            }

            ButtonState.Completed -> {
                labelText = context.getString(R.string.done)
                invalidate()
            }

            ButtonState.Reset -> {
                progressPercentage = 0.0
                labelText = context.getString(R.string.download)
                invalidate()
            }
        }
    }

    // Paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Colors
    private var buttonBackgroundColor = 0
    private var buttonFillingColor = 0
    private var textColor = 0

    init {
        isClickable = true
        labelText = context.getString(R.string.download)

        // Assign the custom Attributes values
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_buttonBackgroundColor, context.getColor(R.color.background))
            buttonFillingColor = getColor(R.styleable.LoadingButton_buttonProgressColor, context.getColor(R.color.purple_700))
            textColor = getColor(R.styleable.LoadingButton_buttonTextColor, context.getColor(R.color.white))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas!!)
        drawProgress(canvas)
        drawText(canvas)
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
    }

    private fun drawProgress(canvas: Canvas) {

        // Draw the filling button progress
        paint.color = buttonFillingColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(0f, 0f, (widthSize * progressPercentage).toFloat(), heightSize.toFloat(), 20f, 20f, paint)
        Log.e("progressPercentage", progressPercentage.toString())
    }

    private fun drawText(canvas: Canvas) {
        paint.color = textColor
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(labelText, (widthSize / 2).toFloat(), (heightSize / 2).toFloat(), paint)
    }
}

sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
    object Reset : ButtonState()
}