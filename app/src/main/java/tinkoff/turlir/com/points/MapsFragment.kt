package tinkoff.turlir.com.points

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_maps.*
import tinkoff.turlir.com.points.base.MvpFragment
import java.util.concurrent.TimeUnit

class MapsFragment: MvpFragment(), OnMapReadyCallback, MapsView {

    @InjectPresenter
    lateinit var presenter: MapsPresenter

    private var map: GoogleMap? = null
    private val cameraMovement: AsyncEvent<CameraPosition>
            = BehaviorEvent(750, TimeUnit.MILLISECONDS)

    private lateinit var disposable: CompositeDisposable

    private val radius: Double by lazy(LazyThreadSafetyMode.NONE) {
        frg_map_root.height / 2.0 / resources.displayMetrics.density
    }

    private var current: ClusterPoint? = null
    private val markers: MutableSet<ClusterPoint> = mutableSetOf()
    private lateinit var clusterManager: ClusterManager<ClusterPoint>

    @ProvidePresenter
    fun provideMapsPresenter(): MapsPresenter {
        return App.holder.tabComponent.mapsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return root
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        disposable.add(cameraMovement.observe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val target = it.target
                presenter.cameraChanged(target.latitude, target.longitude, it.zoom.toDouble(), radius)
            })
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    override fun onRequestPermissionsResult(code: Int, key: Array<String>, value: IntArray) {
        var located = false
        if (value.isNotEmpty() &&
            value.first() == PackageManager.PERMISSION_GRANTED) {
            presenter.startWithPermission()
            located = true
        } else {
            presenter.strictStart()
        }
        applyLocationSettings(located)
    }

    override fun onMapReady(google: GoogleMap) {
        map = google
        google.uiSettings?.run {
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
        google.setOnCameraMoveListener {
            map?.cameraPosition?.let {
                cameraMovement.push(it)
            }
        }
        clusterManager = ClusterManager(requireContext(), google)
        clusterManager.renderer = MapPointRender(requireContext(), google, clusterManager)
        clusterManager.setOnClusterItemClickListener { point ->
            if (current != null && point != current) {
                clusterManager.removeItem(current)
                current?.icon = BitmapDescriptorFactory.defaultMarker()
                clusterManager.addItem(current)
            }
            current = point
            clusterManager.removeItem(current)
            point.icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            clusterManager.addItem(current)
            clusterManager.cluster()
            true
        }
        google.setOnCameraIdleListener(clusterManager)
        google.setOnMarkerClickListener(clusterManager)

        val granted = checkLocation()
        applyLocationSettings(granted)
    }

    override fun moveToLocation(location: Location) {
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                DEFAULT_ZOOM
            )
        )
        map?.cameraPosition?.let {
            cameraMovement.push(it)
        }
    }

    override fun renderMarkers(points: List<MapsPoint>) {
        // clear
        val mass = markers.iterator()
        while (mass.hasNext()) {
            val marker = mass.next()
            if (marker != current) {
                clusterManager.removeItem(marker)
                mass.remove()
            }
        }
        // render new
        for (point in points) {
            if (point != current?.point) {
                val item = ClusterPoint(point)
                clusterManager.addItem(item)
                markers.add(item)
            }
        }
        clusterManager.cluster() // update
    }

    override fun error(desc: String) {
        view?.let {
            Snackbar.make(it, desc, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun checkLocation(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return if (permission == PackageManager.PERMISSION_GRANTED) {
            presenter.startWithPermission()
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun applyLocationSettings(granted: Boolean) {
        val safeMap = map ?: return
        safeMap.isMyLocationEnabled = granted
        safeMap.uiSettings.isMyLocationButtonEnabled = granted
    }

    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val LOCATION_REQUEST = 38
    }
}