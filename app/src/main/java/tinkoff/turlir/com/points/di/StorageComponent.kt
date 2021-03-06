package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.base.DpiProvider
import tinkoff.turlir.com.points.base.PointHolder
import tinkoff.turlir.com.points.storage.AppDatabase
import tinkoff.turlir.com.points.storage.Repository
import tinkoff.turlir.com.points.storage.SharedPrefs
import javax.inject.Singleton

@Component(modules = [StorageModule::class], dependencies = [AppComponent::class])
@Singleton
interface StorageComponent: AppComponent {

    fun repository(): Repository

    fun database(): AppDatabase

    fun prefs(): SharedPrefs

    fun dpiProvider(): DpiProvider

    fun pointHolder(): PointHolder
}