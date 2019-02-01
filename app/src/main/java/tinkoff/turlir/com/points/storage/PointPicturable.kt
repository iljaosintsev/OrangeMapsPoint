package tinkoff.turlir.com.points.storage

import tinkoff.turlir.com.points.maps.MapsPoint

data class PointPicturable(
    val point: MapsPoint,
    val partner: Partner
) {
    fun picture(density: String): String {
        return point.picture(partner.picture, density)
    }
}