package tinkoff.turlir.com.points

import android.location.Location
import com.arellomobile.mvp.MvpView

interface MapsView: MvpView {

    fun moveToLocation(location: Location)

    fun error(desc: String)
}