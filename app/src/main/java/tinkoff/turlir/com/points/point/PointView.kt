package tinkoff.turlir.com.points.point

import tinkoff.turlir.com.points.base.BaseMvpView

interface PointView: BaseMvpView {

    fun viewed(flag: Boolean)
}