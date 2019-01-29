package tinkoff.turlir.com.points.point

import com.arellomobile.mvp.MvpView
import tinkoff.turlir.com.points.maps.MapsPoint
import tinkoff.turlir.com.points.storage.Partner

interface PointView: MvpView {

    fun renderPoint(point: MapsPoint, partner: Partner)

    fun notFound()

    fun error(message: String)
}