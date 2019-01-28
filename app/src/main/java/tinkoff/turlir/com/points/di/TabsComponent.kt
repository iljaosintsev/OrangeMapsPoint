package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.maps.MapsPresenter

@Component(dependencies = [StorageComponent::class])
@TabScope
interface TabsComponent {

    fun mapsPresenter(): MapsPresenter
}