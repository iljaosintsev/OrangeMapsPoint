package tinkoff.turlir.com.points.storage

import tinkoff.turlir.com.points.maps.MapsPoint

data class PointPicturable(
    val point: MapsPoint,
    val picture: String
) {
    fun picture(density: String): String {
        return "https://static.tinkoff.ru/icons/deposition-partners-v3/$density/$picture"
    }
}