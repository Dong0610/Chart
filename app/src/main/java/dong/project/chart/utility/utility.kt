package dong.project.chart

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent



import kotlin.math.roundToInt


fun roundToFirstDecimal(number: Double): Double {
    return (number * 10).roundToInt() / 10.0
}

fun getFirstValueDecimal(number: Double): Double {
    return (number * 10.0).toInt() / 10.0
}


fun Canvas.drawTriangle(
    borderPaint: Paint,
    firstPoint: PointF,
    secondPoint: PointF,
    threePoint: PointF
) {
    val shapePath = Path()
    shapePath.moveTo(firstPoint.x, firstPoint.y)
    shapePath.lineTo(secondPoint.x, secondPoint.y)
    shapePath.lineTo(threePoint.x, threePoint.y)
    shapePath.close()
    drawPath(shapePath, borderPaint)
}


fun Canvas.drawRoundRectPath(
    rectF: RectF,
    radius: Float,
    topLeft: Boolean,
    topRight: Boolean,
    bottomLeft: Boolean,
    bottomRight: Boolean,
    paint: Paint
) {
    val path = Path()
    if (bottomRight) {
        path.moveTo(rectF.left, rectF.bottom - radius)
    } else {
        path.moveTo(rectF.left, rectF.bottom)
    }
    if (topLeft) {
        path.lineTo(rectF.left, rectF.top + radius)
        path.quadTo(rectF.left, rectF.top, rectF.left + radius, rectF.top)
    } else {
        path.lineTo(rectF.left, rectF.top)
    }
    if (topRight) {
        path.lineTo(rectF.right - radius, rectF.top)
        path.quadTo(rectF.right, rectF.top, rectF.right, rectF.top + radius)
    } else {
        path.lineTo(rectF.right, rectF.top)
    }
    if (bottomRight) {
        path.lineTo(rectF.right, rectF.bottom - radius)
        path.quadTo(rectF.right, rectF.bottom, rectF.right - radius, rectF.bottom)
    } else {
        path.lineTo(rectF.right, rectF.bottom)
    }
    if (bottomLeft) {
        path.lineTo(rectF.left + radius, rectF.bottom)
        path.quadTo(rectF.left, rectF.bottom, rectF.left, rectF.bottom - radius)
    } else {
        path.lineTo(rectF.left, rectF.bottom)
    }
    path.close()

    drawPath(path, paint)
}

fun Canvas.drawTriangle(
    borderPaint: Paint,
    firstPoint: Point,
    secondPoint: Point,
    threePoint: Point
) {
    val shapePath = Path()
    shapePath.moveTo(firstPoint.x.toFloat(), firstPoint.y.toFloat())
    shapePath.lineTo(secondPoint.x.toFloat(), secondPoint.y.toFloat())
    shapePath.lineTo(threePoint.x.toFloat(), threePoint.y.toFloat())
    shapePath.close()
    drawPath(shapePath, borderPaint)
}
