package tinkoff.turlir.com.points

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tinkoff.turlir.com.points.maps.Radiator

class RadiatorTest {

    private lateinit var radiator: Radiator

    @Before
    fun setup() {
        radiator = Radiator()
    }

    @Test
    fun computeDistanceRandom() {
        val top = LatLng(57.024977692567134, 61.45873457193374)
        val bottom = LatLng(57.01069337050285, 61.45873457193374)

        val distance = radiator.radius(top, bottom)
        assertEquals(1588, distance)
    }

    @Test
    fun computePixelRandom() {
        val lat = 57.024977692567134
        val zoom = 15.0
        val pixels = 2140
        val dp = 3.5
        val dip = pixels / dp

        val distance = radiator.radius(lat, zoom, dip)
        assertEquals(1590, distance)
    }
}