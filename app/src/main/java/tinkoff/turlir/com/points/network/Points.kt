package tinkoff.turlir.com.points.network

class PointsContainer(override val payload: List<Point>) : Container<Point>()

data class Point(
    val externalId: String,
    val partnerName: String,
    val workHours: String?,
    val addressInfo: String?,
    val fullAddress: String,
    val location: PointLocation
)

data class PointLocation(
    val latitude: Double,
    val longitude: Double
)
