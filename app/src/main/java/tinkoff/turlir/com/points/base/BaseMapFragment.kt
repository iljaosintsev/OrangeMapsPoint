package tinkoff.turlir.com.points.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import tinkoff.turlir.com.points.R

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 01.03.17
 */
abstract class BaseMapFragment : Fragment(), OnMapReadyCallback {

    private var isGoogleMapReady = false
    private var alreadyStateSaved = false
    private val mvpDelegate: MvpDelegate<out BaseMapFragment> by lazy { MvpDelegate(this) }

    @get:LayoutRes
    protected abstract val layout: Int

    private var lastPosition: CameraPosition? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
        lastPosition = savedInstanceState?.getParcelable("KEY")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        isGoogleMapReady = false
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        alreadyStateSaved = false
        if (isGoogleMapReady) {
            mvpDelegate.onAttach()
        }
    }

    override fun onResume() {
        super.onResume()

        alreadyStateSaved = false
        if (isGoogleMapReady) {
            mvpDelegate.onAttach()
        }
    }

    override fun onStop() {
        super.onStop()

        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mvpDelegate.onDetach()
        mvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity!!.isFinishing) {
            mvpDelegate.onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isStateSaved) {
            alreadyStateSaved = false
            return
        }

        if (isRemoving) {
            mvpDelegate.onDestroy()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        alreadyStateSaved = true
        outState.putParcelable("KEY", onLastPosition())
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    abstract fun onLastPosition(): CameraPosition

    override fun onMapReady(google: GoogleMap) {
        isGoogleMapReady = true
        mvpDelegate.childrenSaveState
        mvpDelegate.onAttach()
        lastPosition?.let {
            google.moveCamera(CameraUpdateFactory.newCameraPosition(it))
        }
    }
}