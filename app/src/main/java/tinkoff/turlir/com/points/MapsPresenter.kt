package tinkoff.turlir.com.points

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.patloew.rxlocation.RxLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MapsPresenter(context: Context): MvpPresenter<MapsView>() {

    private val cnt = context.applicationContext

    @SuppressLint("MissingPermission")
    fun startWithPermission() {
        RxLocation(cnt)
            .location()
            .lastLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableMaybeObserver<Location>() {
                override fun onSuccess(location: Location) {
                    dispose()
                    viewState.moveToLocation(location)
                }

                override fun onComplete() {
                    dispose()
                    strictStart()
                }

                override fun onError(e: Throwable) {
                    dispose()
                    e.message?.let {
                        viewState.error(it)
                    }
                }
            })
    }

    fun strictStart() {
        // fallback without permission
    }
}