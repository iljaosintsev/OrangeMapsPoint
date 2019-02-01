package tinkoff.turlir.com.points.storage

import androidx.room.Embedded

data class PointMergePartner(
    @Embedded
    val point: DataPoint,
    @Embedded
    val partner: Partner
)
