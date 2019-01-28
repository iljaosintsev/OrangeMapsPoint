package tinkoff.turlir.com.points.maps

import com.google.android.gms.maps.model.LatLng

data class MapsPoint(
    val externalId: String,
    val partnerName: String,
    val workHours: String?,
    val addressInfo: String?,
    val fullAddress: String,
    val location: LatLng
)