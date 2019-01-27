package tinkoff.turlir.com.points.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : MvpView?>: MvpPresenter<T>() {

    protected lateinit var disposed: CompositeDisposable

    override fun attachView(view: T) {
        super.attachView(view)
        disposed = CompositeDisposable()
    }

    override fun detachView(view: T) {
        super.detachView(view)
        disposed.clear()
    }

    infix operator fun CompositeDisposable.plus(resource: Disposable) {
        this.add(resource)
    }
}