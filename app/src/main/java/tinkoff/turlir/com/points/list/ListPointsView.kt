package tinkoff.turlir.com.points.list

import tinkoff.turlir.com.points.base.BaseMvpView
import tinkoff.turlir.com.points.storage.PointPicturable

interface ListPointsView: BaseMvpView {

    fun presentPoints(points: List<PointPicturable>)

    fun empty()
}
