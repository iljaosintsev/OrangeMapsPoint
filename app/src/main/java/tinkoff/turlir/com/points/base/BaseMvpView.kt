package tinkoff.turlir.com.points.base

import com.arellomobile.mvp.MvpView

interface BaseMvpView: MvpView {

    fun error(message: String)
}