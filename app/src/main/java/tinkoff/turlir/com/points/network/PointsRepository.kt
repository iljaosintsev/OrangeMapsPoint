package tinkoff.turlir.com.points.network

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import tinkoff.turlir.com.points.MapsPoint
import javax.inject.Inject

class PointsRepository @Inject constructor(private val network: Network) {

    fun partners(type: String = "Credit"): Single<List<Partner>> {
        return network.partners(type)
            .map { it.payload }
    }

    fun loadPoints(
        latitude: Double,
        longitude: Double,
        radius: Int,
        partners: String? = null
    ): Single<List<MapsPoint>> {

        return network.points(latitude, longitude, radius, partners)
            .map {
                it.payload.map { item ->
                    MapsPoint(
                        externalId = item.externalId,
                        partnerName = item.partnerName,
                        workHours = item.workHours,
                        addressInfo = item.addressInfo,
                        fullAddress = item.fullAddress,
                        location = LatLng(item.location.latitude, item.location.longitude)
                    )
                }
            }
    }
}