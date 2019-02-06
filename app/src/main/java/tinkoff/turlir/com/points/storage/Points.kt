package tinkoff.turlir.com.points.storage

import com.google.gson.annotations.SerializedName

class PointsContainer(override val payload: List<Point>) : Container<Point>()

data class Point(
    @field:SerializedName("externalId")
    val externalId: String,
    @field:SerializedName("partnerName")
    val partnerName: String,
    @field:SerializedName("workHours")
    val workHours: String?,
    @field:SerializedName("addressInfo")
    val addressInfo: String?,
    @field:SerializedName("fullAddress")
    val fullAddress: String,
    @field:SerializedName("location")
    val location: PointLocation
)

data class PointLocation(
    val latitude: Double,
    val longitude: Double
)
