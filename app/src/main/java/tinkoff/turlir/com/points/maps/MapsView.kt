package tinkoff.turlir.com.points.maps

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import tinkoff.turlir.com.points.base.BaseMvpView
import tinkoff.turlir.com.points.storage.Partner

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MapsView: BaseMvpView {

    fun renderMarkers(points: List<MapsPoint>)

    fun renderPartner(partner: Partner)
}