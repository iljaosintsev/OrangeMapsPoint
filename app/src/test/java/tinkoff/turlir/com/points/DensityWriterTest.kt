package tinkoff.turlir.com.points

import android.util.DisplayMetrics
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DensityWriterTest {

    private lateinit var writer: DensityWriter

    @Before
    fun setUp() {
        writer = DensityWriter()
    }

    @Test
    fun applyAllDensity() {
        val map = mapOf(
            DisplayMetrics.DENSITY_LOW to "mdpi",
            DisplayMetrics.DENSITY_MEDIUM to "mdpi",
            DisplayMetrics.DENSITY_TV to "mdpi",

            DisplayMetrics.DENSITY_HIGH to "hdpi",
            DisplayMetrics.DENSITY_260 to "hdpi",
            DisplayMetrics.DENSITY_280 to "hdpi",
            DisplayMetrics.DENSITY_300 to "hdpi",

            DisplayMetrics.DENSITY_XHIGH to "xhdpi",
            DisplayMetrics.DENSITY_340 to "xhdpi",
            DisplayMetrics.DENSITY_360 to "xhdpi",
            DisplayMetrics.DENSITY_400 to "xhdpi",
            DisplayMetrics.DENSITY_420 to "xhdpi",
            DisplayMetrics.DENSITY_440 to "xhdpi",

            DisplayMetrics.DENSITY_XXHIGH to "xxhdpi",
            DisplayMetrics.DENSITY_560 to "xxhdpi",

            DisplayMetrics.DENSITY_XXXHIGH to "xxxhdpi",

            DisplayMetrics.DENSITY_DEFAULT to "mdpi"
        )
        for ((value, text) in map) {
            val apply = writer.apply(value)
            assertEquals("$text and $value", text, apply)
        }
    }

    @Test
    fun applyInvalidDensity() {
        assertEquals("mdpi", writer.apply(1024))
        assertEquals("mdpi", writer.apply(-2048))
    }
}