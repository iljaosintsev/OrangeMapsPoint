package tinkoff.turlir.com.points.maps

import android.location.Location
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import tinkoff.turlir.com.points.storage.Partner

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MapsView: MvpView {

    fun moveToLocation(location: Location)

    fun renderMarkers(points: List<MapsPoint>)

    fun renderPartner(partner: Partner)

    fun error(desc: String)
}