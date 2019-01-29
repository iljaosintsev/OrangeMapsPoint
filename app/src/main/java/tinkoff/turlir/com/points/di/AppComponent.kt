package tinkoff.turlir.com.points.di

import android.content.Context
import dagger.Component
import tinkoff.turlir.com.points.base.DensitySaturation
import tinkoff.turlir.com.points.base.DensityWriter
import tinkoff.turlir.com.points.maps.Radiator

@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context

    fun radiator(): Radiator

    fun densityWriter(): DensityWriter

    fun densitySaturation(): DensitySaturation
}