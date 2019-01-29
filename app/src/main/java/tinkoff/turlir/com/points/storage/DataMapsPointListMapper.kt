package tinkoff.turlir.com.points.storage

import com.google.android.gms.maps.model.LatLng
import io.reactivex.functions.Function
import tinkoff.turlir.com.points.maps.MapsPoint

class DataMapsPointMapper : Function<DataPoint, MapsPoint> {

    override fun apply(item: DataPoint): MapsPoint {
        return MapsPoint(
            externalId = item.externalId,
            partnerName = item.partnerName,
            workHours = item.workHours,
            addressInfo = item.addressInfo,
            fullAddress = item.fullAddress,
            location = LatLng(item.latitude, item.longitude),
            viewed = item.viewed
        )
    }
}

class DataMapsPointListMapper(private val delegate: DataMapsPointMapper) :
    Function<List<DataPoint>, List<MapsPoint>> {

    override fun apply(t: List<DataPoint>): List<MapsPoint> {
        return t.map(delegate::apply)
    }
}