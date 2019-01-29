package tinkoff.turlir.com.points.list

import com.arellomobile.mvp.MvpView
import tinkoff.turlir.com.points.storage.PointPicturable

interface ListPointsView: MvpView {

    fun presentPoints(points: List<PointPicturable>)

    fun empty()

    fun error(message: String)
}
