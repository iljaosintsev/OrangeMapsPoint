package tinkoff.turlir.com.points.maps

import android.location.Location
import com.arellomobile.mvp.MvpView
import tinkoff.turlir.com.points.network.Partner

interface MapsView: MvpView {

    fun moveToLocation(location: Location)

    fun renderMarkers(points: List<MapsPoint>)

    fun renderPartner(partner: Partner)

    fun error(desc: String)
}