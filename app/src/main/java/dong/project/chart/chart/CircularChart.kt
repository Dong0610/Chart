package dong.project.chart.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import dong.project.chart.base.BaseChart
import dong.project.chart.drawRoundRectPath
import dong.project.chart.utility.Colors
import java.io.Serializable

class CircularChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseChart(context, attrs, defStyleAttr) {

    private var listChartData = mutableListOf<ChartData>()
    private val listColor = mutableListOf<Int>(
        Colors.RED,
        Colors.ORANGE,
        Colors.GOLD,
        Colors.LIME_GREEN,
        Colors.ROYAL_BLUE,
        Colors.SKY_BLUE,
        Colors.PURPLE,
        Colors.VIOLET,
        Colors.CHOCOLATE,
        Colors.CYAN,
        Colors.GRAY,
        Colors.IVORY
    )
    private var total = 0f

    fun setData(listData: MutableList<ChartData>): CircularChart {
        this.listChartData = listData
        initData()
        invalidate()
        return this
    }

    private var chartName = ""

    fun chartName(name: String): CircularChart {
        chartName = name
        invalidate()
        return this
    }

    private fun drawChartName(canvas: Canvas) {
        if (chartName != "") {
            textPaint.apply {
                textSize = canvas.width * 0.036f
            }
            val widthDraw = width * 0.9f
            canvas.drawText(chartName, width * 0.1f + widthDraw / 2, height * 0.96f, textPaint)
        }
        invalidate()
    }

    data class ChartData(var value: Float = 0f, var name: String = "") : Serializable

    private var listData = mutableListOf<Float>()
    private fun initData() {
        listChartData.forEach {
            listData.add(it.value)
            total += it.value
        }
    }

    private var isGloss = false
    fun setGloss(isGloss: Boolean): CircularChart {
        this.isGloss = isGloss
        invalidate()
        return this
    }

    override fun getTypedArr() {

    }

    override fun dispatchDraw(canvas: Canvas) {
        drawCircle(canvas)
        drawChartName(canvas)
        if (isGloss) {
            drawGloss(canvas)
            drawSelectCircle(canvas)
            drawValueSelect(canvas)
        }
        super.dispatchDraw(canvas)
    }

    private fun drawValueSelect(canvas: Canvas) {
        val rectF = RectF(width * 0.01f, height * 0.01f, width * 0.18f, height * 0.09f)
        canvas.drawRoundRectPath(rectF, rectF.width() / 8f, true, true, true, true, Paint().apply {
            strokeWidth = 1f
            color = Color.parseColor("#F1F1F1")
            style = Paint.Style.FILL
        })

        fun formatNumber(number: Float): String {
            return String.format("%.1f", number)
        }

        var value = ""
        if (selectValue != -1) {
            listData.forEachIndexed { index, fl ->
                if (index == selectValue) {
                    value = "${formatNumber((fl / total) * 100f)}/100%"
                }
            }
        } else {
            value = "0/100%"
        }
        textPaint.apply {
            color = Color.BLACK
            textSize = rectF.height() * 0.4f
            textAlign = Paint.Align.CENTER
        }

        val textX = rectF.centerX()
        val textY = rectF.centerY() - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(value, textX, textY, textPaint)
    }


    private lateinit var rectF: RectF
    private var listSelectRect = mutableListOf<RectF>()
    private fun drawGloss(canvas: Canvas) {
        listSelectRect.clear()
        val glossWidth = rectF.width() / 6
        var startDrawY = height * 0.05f
        val startDrawX = height * 0.75f
        listChartData.forEachIndexed { index, chartData ->
            val rectF =
                RectF(startDrawX, startDrawY, startDrawX + glossWidth, startDrawY + glossWidth)

            val sweepAngle = glossWidth * 0.28f
            val paint = Paint().apply {
                color = listColor.getOrElse(index) { Color.GRAY }
                style = Paint.Style.FILL
            }
            canvas.drawText(
                chartData.name,
                rectF.right,
                rectF.top + rectF.height() * 0.8f,
                textPaint.apply {
                    textSize = canvas.width * 0.030f
                })

            listSelectRect.add(
                RectF(
                    startDrawX,
                    rectF.top,
                    width,
                    rectF.top + rectF.height() * 0.8f
                )
            )
            canvas.drawArc(rectF, -sweepAngle, sweepAngle, true, paint)
            startDrawY += glossWidth * 0.82f
        }
    }

    private var selectValue = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                findTouchedValue(event.x, event.y) {
                    selectValue = if (it == selectValue) -1 else it
                }
            }
        }
        invalidate()
        return true
    }


    private fun findTouchedValue(x: Float, y: Float, calback: (Int) -> Unit) {
        listSelectRect.forEachIndexed { index, rectF ->
            if (y <= rectF.bottom && y >= rectF.top && x >= rectF.left) {
                calback(index)
            }
        }
    }

    private fun drawCircle(canvas: Canvas) {
        val canvasSize =
            if (canvas.width > canvas.height) canvas.height.toFloat() else canvas.width.toFloat()
        val circularWidthMultiplier = 0.36f

        val centerX = width * 0.15f + canvasSize / 4f
        val centerY = height * 0.45f
        val radius = canvasSize * circularWidthMultiplier

        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        this.rectF = rectF
        var startAngle = -90f

        listData.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val paint = Paint().apply {
                color = listColor.getOrElse(index) { Color.GRAY }
                style = Paint.Style.FILL
            }
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
            startAngle += sweepAngle
        }
        invalidate()
    }

    private fun drawSelectCircle(canvas: Canvas) {
        val canvasSize =
            if (canvas.width > canvas.height) canvas.height.toFloat() else canvas.width.toFloat()
        val circularWidthMultiplier = 0.38f

        val centerX = width * 0.15f + canvasSize / 4f
        val centerY = height * 0.45f
        val radius = canvasSize * circularWidthMultiplier

        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        this.rectF = rectF
        var startAngle = -90f

        listData.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val paint = Paint().apply {
                color = listColor.getOrElse(index) { Color.GRAY }
                style = Paint.Style.FILL
            }
            if (index == selectValue) {
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
            }
            startAngle += sweepAngle
        }
        invalidate()
    }

}
