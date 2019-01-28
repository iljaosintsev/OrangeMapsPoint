package tinkoff.turlir.com.points.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class DataPoint(
    @PrimaryKey
    val externalId: String,
    val partnerName: String,
    val workHours: String?,
    val addressInfo: String?,
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val viewed: Boolean
)