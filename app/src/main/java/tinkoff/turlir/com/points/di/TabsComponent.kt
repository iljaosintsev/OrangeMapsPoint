package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.list.ListPresenter
import tinkoff.turlir.com.points.maps.CacheValidator
import tinkoff.turlir.com.points.maps.MapsPresenter
import tinkoff.turlir.com.points.point.PointPresenter

@Component(dependencies = [StorageComponent::class])
@TabScope
interface TabsComponent {

    fun mapsPresenter(): MapsPresenter

    fun pointPresenter(): PointPresenter

    fun listPresenter(): ListPresenter

    fun cacheValidator(): CacheValidator
}