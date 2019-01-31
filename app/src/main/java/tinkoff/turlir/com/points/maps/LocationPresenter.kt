package tinkoff.turlir.com.points.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.patloew.rxlocation.RxLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.base.PermissionChecker
import tinkoff.turlir.com.points.di.TabScope
import javax.inject.Inject

@InjectViewState
@TabScope
class LocationPresenter @Inject constructor(
    context: Context,
    private val validator: CacheValidator,
    private val permission: PermissionChecker
) : BasePresenter<LocationView>() {

    private val cnt = context.applicationContext

    private val permissionCallback = object : PermissionChecker.Callback {
        override fun granted() {
            viewState.permissionGranted(true)
            startWithPermission()
        }

        override fun denied() {
            viewState.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        permission.check(Manifest.permission.ACCESS_FINE_LOCATION, permissionCallback)
    }

    fun startWithPermission() {
        disposed + validator.validate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.permissionGranted(true)
                location()
            }, ::handleError)
    }

    fun strictStart() {
        disposed + validator.validate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.permissionGranted(false)
            }, ::handleError)
    }

    @SuppressLint("MissingPermission")
    private fun location() {
        RxLocation(cnt)
            .location()
            .lastLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableMaybeObserver<Location>() {
                override fun onSuccess(location: Location) {
                    dispose()
                    Log.v("LocationPresenter", location.toString())
                    viewState.moveToLocation(location)
                }

                override fun onComplete() {
                    dispose()
                    Log.w("LocationPresenter", "location not found")
                    strictStart()
                }

                override fun onError(e: Throwable) {
                    dispose()
                    handleError(e)
                }
            })
    }
}