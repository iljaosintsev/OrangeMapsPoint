package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.storage.AppDatabase
import tinkoff.turlir.com.points.storage.Repository
import javax.inject.Singleton

@Component(modules = [StorageModule::class], dependencies = [AppComponent::class])
@Singleton
interface StorageComponent: AppComponent {

    fun repository(): Repository

    fun database(): AppDatabase
}