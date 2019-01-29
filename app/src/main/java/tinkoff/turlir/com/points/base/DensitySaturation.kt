package tinkoff.turlir.com.points.base

import android.util.DisplayMetrics
import io.reactivex.functions.Function

class DensitySaturation(
    private val min: Int = DisplayMetrics.DENSITY_MEDIUM,
    private val max: Int = DisplayMetrics.DENSITY_560
): Function<Int, Int> {

    init {
        if (min > max) {
            throw IllegalArgumentException()
        }
    }

    override fun apply(value: Int): Int {
        return Math.min(
            Math.max(value, min),
            max
        )
    }
}