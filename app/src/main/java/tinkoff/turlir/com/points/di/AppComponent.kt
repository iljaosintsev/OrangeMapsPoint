package tinkoff.turlir.com.points.di

import android.content.Context
import com.google.android.gms.maps.model.CameraPosition
import dagger.Component
import tinkoff.turlir.com.points.base.AsyncEvent
import tinkoff.turlir.com.points.base.DensitySaturation
import tinkoff.turlir.com.points.base.DensityWriter

@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context

    fun densityWriter(): DensityWriter

    fun densitySaturation(): DensitySaturation

    fun asyncEvent(): AsyncEvent<CameraPosition>
}