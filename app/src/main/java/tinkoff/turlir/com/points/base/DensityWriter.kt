package tinkoff.turlir.com.points.base

import android.util.DisplayMetrics
import io.reactivex.functions.Function

class DensityWriter : Function<Int, String> {

    override fun apply(density: Int): String {
        return when (density) {
            in DisplayMetrics.DENSITY_LOW..DisplayMetrics.DENSITY_TV -> "mdpi"
            in DisplayMetrics.DENSITY_HIGH..DisplayMetrics.DENSITY_300 -> "hdpi"
            in DisplayMetrics.DENSITY_XHIGH..DisplayMetrics.DENSITY_440 -> "xhdpi"
            in DisplayMetrics.DENSITY_XXHIGH..DisplayMetrics.DENSITY_560 -> "xxhdpi"
            DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
            else -> "mdpi"
        }
    }
}