package tinkoff.turlir.com.points

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MapPointRender(
    cnt: Context,
    map: GoogleMap,
    manager: ClusterManager<ClusterPoint>
) : DefaultClusterRenderer<ClusterPoint>(cnt, map, manager) {

    override fun onClusterItemRendered(item: ClusterPoint, marker: Marker) {
        super.onClusterItemRendered(item, marker)
        marker.setIcon(item.icon)
    }
}