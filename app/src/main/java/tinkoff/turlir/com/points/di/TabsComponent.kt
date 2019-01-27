package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.MapsPresenter

@Component(dependencies = [AppComponent::class])
@TabScope
interface TabsComponent {

    fun mapsPresenter(): MapsPresenter
}