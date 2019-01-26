package tinkoff.turlir.com.points

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsFragment: Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return root
    }

    override fun onRequestPermissionsResult(code: Int, key: Array<String>, value: IntArray) {
        var located = false
        if (value.isNotEmpty() &&
            value.first() == PackageManager.PERMISSION_GRANTED) {
            located = true
        }
        applyLocationSettings(located)
    }

    override fun onMapReady(google: GoogleMap) {
        map = google
        map?.uiSettings?.run {
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(DEBUG_CITY, DEFAULT_ZOOM)
        )

        val granted = checkLocation()
        applyLocationSettings(granted)
    }

    private fun checkLocation(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return if (permission == PackageManager.PERMISSION_GRANTED) {
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
        private const val DEFAULT_ZOOM = 10f
        private val DEBUG_CITY = LatLng(56.838011, 60.597465)
        private const val LOCATION_REQUEST = 38
    }
}