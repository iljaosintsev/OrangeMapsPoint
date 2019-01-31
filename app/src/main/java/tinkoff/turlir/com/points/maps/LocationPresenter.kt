package tinkoff.turlir.com.points.maps

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.common.api.Result
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.patloew.rxlocation.StatusException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.base.PermissionChecker
import tinkoff.turlir.com.points.di.TabScope
import javax.inject.Inject

@InjectViewState
@TabScope
class LocationPresenter @Inject constructor(
    private val validator: CacheValidator,
    private val permission: PermissionChecker,
    private val locator: RxLocation
) : BasePresenter<LocationView>() {

    private val request = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        .setNumUpdates(1)

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
                checkLocationSettings()
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

    private fun checkLocationSettings() {
        disposed + locator.settings()
            .check(request)
            .subscribe({ result ->
                handleLocationSettingsResult(result)

            }, { error ->
                if (error is StatusException) {
                    handleLocationSettingsResult(error.result)
                } else {
                    handleError(error)
                }
            })
    }

    @SuppressLint("MissingPermission")
    fun acquireLocation() {
        disposed + locator.location()
            .updates(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ location ->
                Log.v("LocationPresenter", location.toString())
                viewState.moveToLocation(location)
            }, ::handleError)
    }

    private fun handleLocationSettingsResult(result: Result) {
        val status = result.status
        if (status.isSuccess) {
            Log.d("LocationPresenter", "location settings good")
            acquireLocation()

        } else if (status.isCanceled || status.isInterrupted) {
            Log.d("LocationPresenter", "checking location setting cancelled or interrupted")
            strictStart()

        } else if (result.status.hasResolution()) {
            Log.d("LocationPresenter", "needs resolution location settings problem")
            viewState.resolutionLocationSettings(status)
        }
    }
}