package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.MapsPresenter

@Component(modules = [AppModule::class])
interface AppComponent {

    fun mapsPresenter(): MapsPresenter
}