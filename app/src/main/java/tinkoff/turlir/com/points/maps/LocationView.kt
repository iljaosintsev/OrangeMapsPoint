package tinkoff.turlir.com.points.maps

import android.location.Location
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import tinkoff.turlir.com.points.base.BaseMvpView

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface LocationView: BaseMvpView {

    @StateStrategyType(value = SkipStrategy::class)
    fun requestPermission(permission: String)

    fun permissionGranted(granted: Boolean)

    @StateStrategyType(value = SkipStrategy::class)
    fun moveToLocation(location: Location)
}
