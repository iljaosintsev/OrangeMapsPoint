package tinkoff.turlir.com.points.di

import android.content.Context
import dagger.Component
import tinkoff.turlir.com.points.Radiator

@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context

    fun radiator(): Radiator
}