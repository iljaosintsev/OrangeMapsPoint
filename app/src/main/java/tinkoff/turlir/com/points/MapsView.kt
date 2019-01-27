package tinkoff.turlir.com.points

import android.location.Location
import com.arellomobile.mvp.MvpView
import tinkoff.turlir.com.points.network.Point

interface MapsView: MvpView {

    fun moveToLocation(location: Location)

    fun renderMarkers(points: List<Point>)

    fun error(desc: String)
}