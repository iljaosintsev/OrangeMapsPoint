package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.network.PointsRepository
import javax.inject.Singleton

@Component(modules = [NetworkModule::class], dependencies = [AppComponent::class])
@Singleton
interface StorageComponent: AppComponent {

    fun repository(): PointsRepository
}