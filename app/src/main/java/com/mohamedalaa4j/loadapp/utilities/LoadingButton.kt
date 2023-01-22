package com.mohamedalaa4j.loadapp.utilities

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.mohamedalaa4j.loadapp.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val animatingCircleCoordinates = RectF()

    var progressPercentage = 0.0
    private var arcPercentage = 0f

    private var labelText = context.getString(R.string.download)

    // Paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Colors
    private var buttonBackgroundColor = 0
    private var buttonFillingColor = 0
    private var textColor = 0

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                valueAnimator.start()
            }

            ButtonState.Loading -> {
                labelText = context.getString(R.string.downloading)
                invalidate()
            }

            ButtonState.Completed -> {
                labelText = context.getString(R.string.done)
                valueAnimator.end()
            }

            ButtonState.Reset -> {
                progressPercentage = 0.0
                arcPercentage = 0f
                labelText = context.getString(R.string.download)
                invalidate()
            }
        }
    }

    // ValueAnimator
    private val valueAnimator: ValueAnimator = ValueAnimator.ofInt(0, 360).apply {
        duration = 500
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE

        addUpdateListener {
            val percentage = it.animatedValue
            arcPercentage = percentage.toString().toFloat()
            invalidate()
        }
    }

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

        setAnimatingCircleCoordinates()
        drawBackgroundArc(canvas)
        drawInnerArc(canvas)

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
    }

    private fun drawText(canvas: Canvas) {
        paint.color = textColor
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(labelText, (widthSize / 2).toFloat(), (heightSize / 1.75).toFloat(), paint)
    }

    private fun drawBackgroundArc(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.color = context.getColor(R.color.background)
        paint.strokeWidth = 15f
        canvas.drawArc(animatingCircleCoordinates, 0f, 360f, false, paint)
    }

    private fun drawInnerArc(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.color = context.getColor(R.color.white)
        paint.strokeWidth = 15f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(animatingCircleCoordinates, 270f, arcPercentage, false, paint)
    }

    private fun setAnimatingCircleCoordinates() {
        val size = 40
        animatingCircleCoordinates.set(
            (width / 1.35).toFloat(),
            (height / 2).toFloat() - size,
            (width / 1.2).toFloat(),
            height.toFloat() - size
        )
    }

}

sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
    object Reset : ButtonState()
}