package tinkoff.turlir.com.points.maps

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterPoint(val point: MapsPoint): ClusterItem {

    var icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker()

    override fun getSnippet(): String {
        return point.addressInfo ?: ""
    }

    override fun getTitle(): String {
        return point.externalId
    }

    override fun getPosition(): LatLng {
        return point.location
    }
}