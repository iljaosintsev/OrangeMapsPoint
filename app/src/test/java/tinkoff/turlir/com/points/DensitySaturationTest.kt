package tinkoff.turlir.com.points

import android.util.DisplayMetrics
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tinkoff.turlir.com.points.base.DensitySaturation

class DensitySaturationTest {

    private lateinit var saturator: DensitySaturation

    @Before
    fun setUp() {
        saturator = DensitySaturation()
    }

    @Test
    fun preSaturation() {
        for (i in -DisplayMetrics.DENSITY_560 .. 0) {
            assertEquals(DisplayMetrics.DENSITY_MEDIUM, saturator.apply(i))
        }
        assertEquals(DisplayMetrics.DENSITY_MEDIUM, saturator.apply(DisplayMetrics.DENSITY_LOW))
    }

    @Test
    fun saturation() {
        for (i in DisplayMetrics.DENSITY_MEDIUM .. DisplayMetrics.DENSITY_560) {
            assertEquals(i, saturator.apply(i))
        }
    }

    @Test
    fun overSaturation() {
        for (i in DisplayMetrics.DENSITY_XXXHIGH .. 2 * DisplayMetrics.DENSITY_XXXHIGH) {
            assertEquals(DisplayMetrics.DENSITY_560, saturator.apply(i))
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalArgument() {
        DensitySaturation(5, 0)
    }
}