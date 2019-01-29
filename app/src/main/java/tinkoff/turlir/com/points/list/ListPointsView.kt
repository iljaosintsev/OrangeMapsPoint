package tinkoff.turlir.com.points.list

import com.arellomobile.mvp.MvpView
import tinkoff.turlir.com.points.maps.MapsPoint

interface ListPointsView: MvpView {

    fun presentPoints(points: List<MapsPoint>)

    fun empty()

    fun error(message: String)
}
