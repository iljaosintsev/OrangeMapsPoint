package tinkoff.turlir.com.points.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.patloew.rxlocation.RxLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.storage.Repository
import java.util.*
import javax.inject.Inject

@InjectViewState
class MapsPresenter @Inject constructor(
    context: Context,
    private val radiator: Radiator,
    private val repo: Repository,
    private val validator: CacheValidator
) : BasePresenter<MapsView>() {

    private val cnt = context.applicationContext

    fun startWithPermission() {
        validator.callback = object: CacheValidator.Callback {
            override fun cacheValid() {
                location()
            }

            override fun cacheCleared() {
                disposed + repo.cachePartner()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        cacheValid()
                    }, { error ->
                        Log.e("MapsPresenter", error.message)
                        error.message?.let {
                            viewState.error(it)
                        }
                    })
            }

            override fun error(error: Throwable) {
                Log.e("MapsPresenter", error.message)
                error.message?.let {
                    viewState.error(it)
                }
            }
        }
        validator.check()
    }

    fun strictStart() {
        // fallback without permission
    }

    fun cameraChanged(lat: Double, long: Double, zoom: Double, distance: Double) {
        val radius = radiator.radius(lat, zoom, distance)
        Log.d("Camera",
            "camera moved\n" +
                    "center = ($lat, $long)\n" +
                    "zoom = $zoom\n" +
                    "radius = $radius"
        )

        disposed + repo.loadPoints(lat, long, radius)
            .map {
                val base = LatLng(lat, long)
                it.sortedWith(
                    PointDistanceComparator(
                        base
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ points ->
                viewState.renderMarkers(points)
            }, { error ->
                Log.e("MapsPresenter", error.message)
                error.message?.let {
                    viewState.error(it)
                }
            })
    }

    fun pointSelected(point: MapsPoint) {
        disposed + repo.partnerById(point.partnerName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({  partner ->
                viewState.renderPartner(partner)
            }, { error ->
                Log.e("MapsPresenter", error.message)
                error.message?.let {
                    viewState.error(it)
                }
            }, {
                Log.w("MapsPresenter", "partner ${point.partnerName} not found")
            })
    }

    @SuppressLint("MissingPermission")
    private fun location() {
        RxLocation(cnt)
            .location()
            .lastLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableMaybeObserver<Location>() {
                override fun onSuccess(location: Location) {
                    dispose()
                    Log.v("MapsPresenter", location.toString())
                    viewState.moveToLocation(location)
                }

                override fun onComplete() {
                    dispose()
                    Log.w("MapsPresenter", "location not found")
                    strictStart()
                }

                override fun onError(e: Throwable) {
                    dispose()
                    Log.e("MapsPresenter", e.message)
                    e.message?.let {
                        viewState.error(it)
                    }
                }
            })
    }

    class PointDistanceComparator(private val base: LatLng): Comparator<MapsPoint> {
        override fun compare(first: MapsPoint, second: MapsPoint): Int {
            val toFirst = SphericalUtil.computeDistanceBetween(base, first.location)
            val toSecond = SphericalUtil.computeDistanceBetween(base, second.location)
            return toFirst.compareTo(toSecond)
        }
    }
}