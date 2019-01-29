package tinkoff.turlir.com.points.point

import tinkoff.turlir.com.points.base.BaseMvpView
import tinkoff.turlir.com.points.maps.MapsPoint
import tinkoff.turlir.com.points.storage.Partner

interface PointView: BaseMvpView {

    fun renderPoint(point: MapsPoint, partner: Partner)

    fun notFound()
}