package tinkoff.turlir.com.points.di

import com.patloew.rxlocation.RxLocation
import dagger.Component
import tinkoff.turlir.com.points.base.PermissionChecker
import tinkoff.turlir.com.points.list.ListPresenter
import tinkoff.turlir.com.points.maps.CacheValidator
import tinkoff.turlir.com.points.maps.LocationPresenter
import tinkoff.turlir.com.points.maps.MapsPresenter
import tinkoff.turlir.com.points.maps.Radiator

@Component(modules = [TabModule::class], dependencies = [StorageComponent::class])
@TabScope
interface TabsComponent {

    fun mapsPresenter(): MapsPresenter

    fun locationPresenter(): LocationPresenter

    fun listPresenter(): ListPresenter

    fun cacheValidator(): CacheValidator

    fun radiator(): Radiator

    fun permissionChecker(): PermissionChecker

    fun rxLocation(): RxLocation
}