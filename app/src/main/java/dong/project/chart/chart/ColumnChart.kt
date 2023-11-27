package dong.duan.customchart.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.size
import dong.project.chart.base.BaseChart
import dong.project.chart.drawRoundRectPath
import dong.project.chart.drawTriangle

class ColumnChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseChart(context, attrs, defStyleAttr) {

    private var listColumn = mutableListOf<RectF>()
    private var listVertical = mutableListOf<Int>()
    private var maxValue = 0

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private var chartName = ""

    private val axisPaint = Paint().apply {
        strokeWidth = 2f
        color = Color.parseColor("#cccCCC")
        style = Paint.Style.FILL
    }

    private var columnPaint = Paint().apply {
        strokeWidth = 1f
        color = Color.parseColor("#cccCCC")
        style = Paint.Style.FILL
    }
    private var listData = mutableListOf<Int>()

    fun setData(listData: MutableList<Int>): ColumnChart {
        listColumn.clear()
        this.listData = listData
        calculateMinMax(listData)
        calculateVerticalValues()
        calculateColumns()
        invalidate()
        return this
    }

    fun columnColor(colors: Int): ColumnChart {
        this.columnPaint.apply {
            color = colors
        }
        invalidate()
        return this
    }

    fun chartName(name: String): ColumnChart {
        chartName = name
        invalidate()
        return this
    }

    private var listHorizontalValue = mutableListOf<String>()

    fun verticalValue(list: MutableList<String>): ColumnChart {
        this.listHorizontalValue = list
        return this
    }

    fun initColumn() {
        calculateMinMax(listData)
        calculateVerticalValues()
        calculateColumns()
        invalidate()
    }

    private fun calculateColumns() {
        listColumn.clear()
        val chartWidth = endX - startX
        val space = chartWidth / (2 * listData.size)
        val colWidth = space
        val chartHeight = startY - endY
        startX += space / 2
        listData.forEachIndexed { index, i ->
            val rectF = RectF()
            val columnHeight = startY - (i.toFloat() / maxValue) * chartHeight
            rectF.left = startX + index * (colWidth + space)
            rectF.top = columnHeight
            rectF.right = rectF.left + colWidth
            rectF.bottom = startY
            listColumn.add(rectF)
        }
    }

    private fun calculateVerticalValues() {
        listVertical.clear()
        listVertical.add(0)
        listVertical.add(maxValue / 4)
        listVertical.add(maxValue / 3)
        listVertical.add(maxValue / 2)
        listVertical.add(maxValue)
    }

    private fun calculateMinMax(listData: MutableList<Int>) {
        maxValue = findMaxByGreaterThan(listData)
    }

    private fun findMaxByGreaterThan(list: List<Int>): Int {
        if (list.isEmpty()) {
            throw NoSuchElementException("List is empty")
        }
        val maxNumber = list.maxOrNull() ?: throw NoSuchElementException("List is empty")

        return if (maxNumber % 5 == 0) {
            maxNumber
        } else {
            val nextGreater = generateSequence(maxNumber + 1) { it + 1 }
                .firstOrNull { it % 5 == 0 }
            nextGreater ?: maxNumber
        }
    }

    override fun getTypedArr() {

    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        startX = width * 0.08f
        startY = height * 0.85f
        endX = width * 0.975f
        endY = height * 0.16f

        textPaint.apply {
            textSize = canvas.width*0.032f
        }

        drawAxits(canvas)
        drawColumns(canvas)
        drawVertical(canvas)
        drawHorValue(canvas)
        drawChartName(canvas)
        if (isGloss) {
            drawGloss(canvas)
        }
        if(isTouch&& columnSelected!=-1 ){
            drawBoxValue(canvas,columnSelected)
        }
        invalidate()
    }

    private fun drawGloss(canvas: Canvas) {
        val textWidth = textPaint.measureText(glName)
        canvas.drawRect(
            width * 0.99f - textWidth * 1.5f,
            height * 0.03f,
            width * 0.99f - textWidth * 1.1f,
            height * 0.05f,
            columnPaint
        )
        canvas.drawText(glName, width * 0.99f - textWidth / 2, height * 0.05f, textPaint)
    }

    private fun drawChartName(canvas: Canvas) {
        if (chartName != "") {
            val widthDraw = width * 0.9f
            canvas.drawText(chartName, width * 0.1f + widthDraw / 2, height * 0.98f, textPaint)
        }
        invalidate()
    }

    private fun drawHorValue(canvas: Canvas) {
        if (listHorizontalValue.size == listColumn.size) {
            listColumn.forEachIndexed { index, rectF ->
                val text = listHorizontalValue[index]

                val centerX = rectF.left + rectF.width() / 2f
                val textX = centerX
                canvas.drawText(text, textX, canvas.height * 0.91f, textPaint)
            }
        } else {
            val width = endX - startX;
            val spaceWidth = width / listHorizontalValue.size
            var startDraw = startX
            listHorizontalValue.forEachIndexed { index, s ->
                val textWidth = textPaint.measureText(s)
                canvas.drawText(
                    s,
                    startDraw + spaceWidth / 2 - textWidth / 2,
                    canvas.height * 0.91f,
                    textPaint
                )
                startDraw += spaceWidth
            }

        }
    }

    private var isGloss = false
    private var glName = ""

    fun setGloss(isGloss: Boolean, glName: String): ColumnChart {
        this.isGloss = isGloss
        this.glName = glName
        return this
    }


    private fun drawAxits(canvas: Canvas) {
        canvas.drawLine(startX, startY, endX, startY, axisPaint)
        canvas.drawLine(startX, startY, startX, endY, axisPaint)
    }

    private var centerPointRect = PointF(0f, 0f)
    private var columnSelected= -1
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                getIndexByPoint(event) { index, currentPoint ->
                    columnSelected = index
                    centerPointRect = currentPoint
                }
            }
        }
        invalidate()
        return true
    }

    fun getIndexByPoint(
        event: MotionEvent,
        calback: (Int, PointF) -> Unit
    ) {
        listColumn.forEachIndexed { index, rectF ->
            if (rectF.left <= event.x && rectF.right > event.x) {
                val pointX = rectF.left + (rectF.right - rectF.left) / 2
                var pointF = PointF(pointX, event.y)
                calback(index, pointF)
            }
        }
    }
    private var isTouch = false
    fun isValue(isShow: Boolean): ColumnChart {
        this.isTouch=isShow
        return this
    }

    private fun drawColumns(canvas: Canvas) {
        initColumn()
        listColumn.forEach { rectF ->
            canvas.drawRoundRectPath(
                rectF,
                rectF.width() / 4,
                true,
                true,
                false,
                false,
                columnPaint
            )
        }
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private fun drawVertical(canvas: Canvas) {
        calculateVerticalValues()

        var startText = startY
        val textWidth = textPaint.measureText(maxValue.toString())
        val linePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 1.5f
        }
        listVertical.forEachIndexed { index, i ->
            canvas.drawText(
                i.toString(),
                width * 0.06f - textWidth / 2f,
                if (i == 0) startText else startText + textWidth / 2f,
                textPaint
            )
            if (i != 0 && i != maxValue) {
                canvas.drawLine(width * 0.072f, startText, width * 0.085f, startText, linePaint)
            }
            startText -= (startY - endY) / (listVertical.size - 1)
        }
    }


    private fun drawBoxValue(
        canvas: Canvas,
        columnSelected: Int
    ) {
        val boxHeight = height * 0.09f
        val boxWidth = boxHeight * 1.72f
        val top = height * 0.015f

        val selectedColumnRect = listColumn.getOrNull(columnSelected)
        val centerX = selectedColumnRect?.let { it.left + it.width() / 2 } ?: centerPointRect.x

        var startPoint = centerX - boxWidth / 2


        val rectF = RectF(startPoint, top, startPoint + boxWidth, top + boxHeight)
        canvas.drawRoundRectPath(
            rectF, rectF.height() / 8f,
            true, true, true, true, columnPaint
        )

        val recDraw = listColumn[columnSelected]
        val bottomPoint = recDraw.top - 10f
        canvas.drawLine(
            centerX,
            top + boxHeight,
            centerX,
            bottomPoint,
            axisPaint
        )

        val text = listData[columnSelected]
        val textPaint = Paint().apply {
            color= Color.WHITE
            textSize = canvas.width*0.034f
            textAlign = Paint.Align.CENTER

        }
        canvas.drawText(text.toString(), centerX, top + boxHeight / 1.6f, textPaint)
    }


}
