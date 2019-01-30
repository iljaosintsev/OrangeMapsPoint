package tinkoff.turlir.com.points.maps

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.AsyncEvent
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.storage.Repository
import java.util.*
import javax.inject.Inject

@InjectViewState
class MapsPresenter @Inject constructor(
    private val radiator: Radiator,
    private val repo: Repository,
    private val cameraMovement: AsyncEvent<CameraPosition>
) : BasePresenter<MapsView>() {

    private var radius: Double = 0.0

    override fun attachView(view: MapsView) {
        super.attachView(view)
        disposed + cameraMovement.observe()
            .subscribe { next ->
                val target = next.target
                cameraChanged(target.latitude, target.longitude, next.zoom.toDouble(), radius)
            }
    }

    fun cameraMove(camera: CameraPosition, radius: Double) {
        this.radius = radius
        cameraMovement.push(camera)
    }

    private fun cameraChanged(lat: Double, long: Double, zoom: Double, distance: Double) {
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
                viewState.renderMarkers(points)
            }, ::handleError)
    }

    fun pointSelected(point: MapsPoint) {
        disposed + repo.partnerById(point.partnerName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({  partner ->
                viewState.renderPartner(partner)
            }, ::handleError, {
                Log.w("MapsPresenter", "partner ${point.partnerName} not found")
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