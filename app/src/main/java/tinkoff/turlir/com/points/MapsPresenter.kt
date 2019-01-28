package tinkoff.turlir.com.points

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
import tinkoff.turlir.com.points.network.Partner
import tinkoff.turlir.com.points.network.PointsRepository
import javax.inject.Inject

@InjectViewState
class MapsPresenter @Inject constructor(
    context: Context,
    private val radiator: Radiator,
    private val repo: PointsRepository
) : BasePresenter<MapsView>() {

    private val cnt = context.applicationContext

    @Volatile
    private var partners = listOf<Partner>()

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
                it.sortedWith(PointDistanceComparator(base))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ points ->
                val ui = points.map {
                    MapsPoint(
                        externalId = it.externalId,
                        partnerName = it.partnerName,
                        workHours = it.workHours,
                        addressInfo = it.addressInfo,
                        fullAddress = it.fullAddress,
                        location = LatLng(it.location.latitude, it.location.longitude)
                    )
                }
                viewState.renderMarkers(ui)
            }, { error ->
                error.message?.let {
                    viewState.error(it)
                }
            })
    }

    fun pointSelected(point: MapsPoint) {
        if (!partners.isEmpty()) {
            val p = partners.find { partner ->
                partner.id == point.partnerName
            }
            if (p != null) {
                viewState.renderPartner(p)
            }
            return
        }
        //
        disposed + repo.partners()
            .map {
                partners = it
                it.find { partner ->
                    partner.id == point.partnerName
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({  partner ->
                if (partner != null) {
                    viewState.renderPartner(partner)
                }
            }, { error ->
                error.message?.let {
                    viewState.error(it)
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