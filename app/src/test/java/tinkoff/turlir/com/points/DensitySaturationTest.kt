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
        for (i in -DisplayMetrics.DENSITY_560 .. DisplayMetrics.DENSITY_560) {
            assertEquals(i, saturator.apply(i))
        }
    }

    @Test
    fun overSaturation() {
        for (i in DisplayMetrics.DENSITY_XXXHIGH .. 2 * DisplayMetrics.DENSITY_XXXHIGH) {
            assertEquals(DisplayMetrics.DENSITY_560, saturator.apply(i))
        }
    }
}