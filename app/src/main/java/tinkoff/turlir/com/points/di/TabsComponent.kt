package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.list.ListPresenter
import tinkoff.turlir.com.points.maps.*

@Component(dependencies = [StorageComponent::class])
@TabScope
interface TabsComponent {

    fun mapsPresenter(): MapsPresenter

    fun locationPresenter(): LocationPresenter

    fun listPresenter(): ListPresenter

    fun cacheValidator(): CacheValidator

    fun radiator(): Radiator

    fun permissionChecker(): PermissionChecker
}