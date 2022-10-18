package com.udacity.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.R
import com.udacity.customview.ButtonState
import com.udacity.customview.Constants.Companion.BtnTextStr
import com.udacity.customview.Constants.Companion.btnColor
import com.udacity.customview.Constants.Companion.btnText
import com.udacity.customview.Constants.Companion.btnTextColor
import com.udacity.customview.Constants.Companion.circleColor
import com.udacity.customview.Constants.Companion.heightSize
import com.udacity.customview.Constants.Companion.loadingColor
import com.udacity.customview.Constants.Companion.loadingText
import com.udacity.customview.Constants.Companion.progress
import com.udacity.customview.Constants.Companion.widthSize
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000)
    var paint= Paint(Paint.ANTI_ALIAS_FLAG)

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        progress=0

        when(new){
            ButtonState.Loading->{
                valueAnimator.start()
                BtnTextStr=loadingText
            }
            ButtonState.Completed->{
                valueAnimator.cancel()
                BtnTextStr=btnText
                progress=0
            }
        }
        invalidate()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            btnColor=getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingColor=getColor(R.styleable.LoadingButton_loadingColor,0)
            btnTextColor=getColor(R.styleable.LoadingButton_buttonTextColor,0)
            circleColor=getColor(R.styleable.LoadingButton_circleColor,0)
            btnText=getString(R.styleable.LoadingButton_buttonText) ?: "Download"
            loadingText=getString(R.styleable.LoadingButton_loadingText) ?:"Loading"
        }

        valueAnimator.apply {
            repeatCount=ValueAnimator.INFINITE
            repeatMode=ValueAnimator.RESTART
            valueAnimator.setDuration(1000)

            valueAnimator.addUpdateListener {
                progress=it.animatedValue as Int
                invalidate()
            }
        }
        buttonState=ButtonState.Completed
        paint.apply {
            style= Paint.Style.FILL
            textSize=80f
            typeface= Typeface.DEFAULT_BOLD
            textAlign= Paint.Align.CENTER
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color=btnColor
        //draw first button
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // draw loading button
        paint.color=loadingColor
        canvas?.drawRect(0f, 0f, (progress*widthSize.toFloat())/360F, heightSize.toFloat(), paint)

        // drawing btn text
        paint.color=btnTextColor
        canvas?.drawText(BtnTextStr, widthSize/2f, heightSize/2f + 25f, paint)

        // drawing the circle
        paint.color= Color.YELLOW
        canvas?.drawArc(
            widthSize / 2f + 220f,
            heightSize/2f - 60f,
            widthSize/2f+300f,
            heightSize/2f+50f,
            0f,
            progress.toFloat(),
            true,
            paint
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}