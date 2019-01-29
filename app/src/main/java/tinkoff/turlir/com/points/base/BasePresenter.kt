package tinkoff.turlir.com.points.base

import android.util.Log
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

abstract class BasePresenter<T : BaseMvpView?>: MvpPresenter<T>() {

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

    protected fun handleError(e: Throwable) {
        Log.e("BasePresenter", "common handleError handler", e)
        e.message?.let {
            viewState!!.error(it)
        }
    }
}