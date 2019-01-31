package tinkoff.turlir.com.points.maps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import tinkoff.turlir.com.points.di.TabScope
import javax.inject.Inject

/**
 * @see [Wiki](https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Resolution_and_Scale)
 * @see [Wiki](https://wiki.openstreetmap.org/wiki/Zoom_levels)
 */
@TabScope
class Radiator @Inject constructor() {

    private val equator = 40075.016686 // km
    private val c = equator * 1000 / 256 // meter/pixel

    /**
     * @param lat – широта на отсчетной точке
     * @param zoom – масштаб карты
     * @param height – длина в DIP
     * @return расстояние в метрах
     */
    fun radius(lat: Double, zoom: Double, height: Double): Int {
        val meterPerPixel = c *
                Math.cos(lat * Math.PI / 180) /
                Math.pow(2.0, zoom)

        return Math.round(meterPerPixel * height).toInt()
    }

    /**
     * @param from – отсечетная точка
     * @param to – конченая точка
     * @return расстояние в метрах между [from] и [to]
     */
    fun radius(from: LatLng, to: LatLng): Int {
        return Math.round(SphericalUtil.computeDistanceBetween(from, to)).toInt()
    }
}