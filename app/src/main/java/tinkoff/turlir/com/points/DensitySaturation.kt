package tinkoff.turlir.com.points

import android.util.DisplayMetrics
import io.reactivex.functions.Function

class DensitySaturation : Function<Int, Int> {

    override fun apply(value: Int): Int {
        if (value > DisplayMetrics.DENSITY_560)
            return DisplayMetrics.DENSITY_560

        return value
    }
}