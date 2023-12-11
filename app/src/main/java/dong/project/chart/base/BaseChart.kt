package dong.project.chart.base

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.setPadding
import dong.project.chart.R

abstract class BaseChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var width = 0f
    var height = 0f
    var leftDef: Int = 0
    var topDef: Int = 0
    var rightDef: Int = 0
    var bottomDef: Int = 0
    var typedArray: TypedArray
    init {
       typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseChart)
        getTypedArr();
        typedArray.recycle()
        setPadding(10)

    }

    fun showToast(mess: Any){
        Toast.makeText(context, mess.toString(), Toast.LENGTH_SHORT).show()
    }

    abstract fun getTypedArr()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = (right - left).toFloat()
        height = (bottom - top).toFloat()
        this.leftDef = left
        this.topDef = top
        this.bottomDef = bottom
        this.rightDef = right
        textPaint.apply {
            textSize = width * 0.032f
        }
        invalidate()
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > maxWidth || measuredHeight > maxHeight) {
            if (measuredWidth > maxWidth) widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY)
            if (measuredHeight > maxHeight) heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
