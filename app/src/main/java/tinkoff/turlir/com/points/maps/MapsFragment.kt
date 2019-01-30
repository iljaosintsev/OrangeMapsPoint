package tinkoff.turlir.com.points.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.point_item.*
import tinkoff.turlir.com.points.App
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.base.BaseMapFragment
import tinkoff.turlir.com.points.base.BehaviorEvent
import tinkoff.turlir.com.points.list.PointInfoHolder
import tinkoff.turlir.com.points.point.PointActivity
import tinkoff.turlir.com.points.storage.Partner
import java.util.concurrent.TimeUnit

class MapsFragment: BaseMapFragment(), MapsView {

    override val layout = R.layout.fragment_maps

    @InjectPresenter
    lateinit var presenter: MapsPresenter

    @ProvidePresenter
    fun provideMapsPresenter(): MapsPresenter {
        return App.holder.tabComponent.get().mapsPresenter()
    }

    private val cameraMovement = BehaviorEvent<CameraPosition>(750, TimeUnit.MILLISECONDS)
    private var current: ClusterPoint? = null
    private val markers: MutableSet<ClusterPoint> = mutableSetOf()

    private val radius: Double by lazy(LazyThreadSafetyMode.NONE) {
        frg_map_root.height / 2.0 / resources.displayMetrics.density
    }

    private val dpi: String by lazy(LazyThreadSafetyMode.NONE) {
        App.holder.storageComponent.dpiProvider().get()
    }

    private lateinit var disposable: CompositeDisposable
    private lateinit var bottomSheetHolder: PointInfoHolder
    private lateinit var clusterManager: ClusterManager<ClusterPoint>
    private lateinit var clusterRender: MapPointRender

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = BottomSheetBehavior.from(frg_map_partner_bottom)
        behavior.isHideable = true
        behavior.state = DEFAULT_SHEET_STATE
        bottomSheetHolder = PointInfoHolder(frg_map_partner_bottom)
        bottomSheetHolder.buttonOpen.setOnClickListener {
            current?.point?.externalId?.let { id->
                val transitionName = getString(R.string.shared_avatar)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    frg_map_icon,
                    getString(R.string.shared_avatar)
                )
                val intent = PointActivity.newIntent(id, transitionName, requireContext())
                startActivity(intent, options.toBundle())
            }
        }
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
        google.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.map_bottom_padding))
        clusterManager = ClusterManager(requireContext(), google)
        clusterRender = MapPointRender(
            requireContext(),
            google,
            clusterManager
        )
        clusterManager.renderer = clusterRender
        clusterManager.setOnClusterItemClickListener { point ->
            current?.let {
                if (point != it) {
                    it.icon = BitmapDescriptorFactory.defaultMarker()
                    clusterRender.updateMarker(it)
                }
            }
            onPointSelected(point)
            true
        }
        google.setOnCameraIdleListener(clusterManager)
        google.setOnMarkerClickListener(clusterManager)

        super.onMapReady(google)
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
        var selected = false
        for (point in points) {
            if (point != current?.point) {
                val item = ClusterPoint(point)
                clusterManager.addItem(item)
                markers.add(item)
                if (!selected && current == null) {
                    onPointSelected(item)
                    selected = true
                }
            }
        }
        clusterManager.cluster() // update
    }

    override fun renderPartner(partner: Partner) {
        Picasso.with(context)
            .cancelRequest(frg_map_icon)

        bottomSheetHolder.bind(partner.picture(dpi))
    }

    override fun error(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
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
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST
            )
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun applyLocationSettings(granted: Boolean) {
        val safeMap = map ?: return
        safeMap.isMyLocationEnabled = granted
        safeMap.uiSettings.isMyLocationButtonEnabled = granted
    }

    private fun onPointSelected(point: ClusterPoint) {
        current = point
        point.icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        clusterRender.updateMarker(point)
        map?.animateCamera(
            CameraUpdateFactory.newLatLng(point.point.location),
            750,
            null
        )

        presenter.pointSelected(point.point)
        val behavior = BottomSheetBehavior.from(frg_map_partner_bottom)
        if (behavior.state == DEFAULT_SHEET_STATE) {
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetHolder.bind(point.point)
    }

    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val DEFAULT_SHEET_STATE = BottomSheetBehavior.STATE_HIDDEN
        private const val LOCATION_REQUEST = 38
    }
}