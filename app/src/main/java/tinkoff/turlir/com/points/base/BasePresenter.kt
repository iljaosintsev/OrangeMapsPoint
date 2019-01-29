package tinkoff.turlir.com.points.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

abstract class BasePresenter<T : MvpView?>: MvpPresenter<T>() {

    protected lateinit var disposed: CompositeDisposable

    override fun attachView(view: T) {
        disposed = CompositeDisposable()
        super.attachView(view)
    }

    override fun detachView(view: T) {
        super.detachView(view)
        disposed.clear()
    }

    infix operator fun DisposableContainer.plus(resource: Disposable) {
        this.add(resource)
    }
}