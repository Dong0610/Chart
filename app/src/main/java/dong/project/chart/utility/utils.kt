package dong.project.chart.utility

import android.util.TypedValue



fun Int.toPx(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    AppContext.context.resources.displayMetrics
)


fun Float.toPx(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    AppContext.context.resources.displayMetrics
)
