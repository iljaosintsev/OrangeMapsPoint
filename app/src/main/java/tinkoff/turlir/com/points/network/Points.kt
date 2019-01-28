package tinkoff.turlir.com.points.network

data class PointsContainer(
    val payload: List<Point>
)

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
