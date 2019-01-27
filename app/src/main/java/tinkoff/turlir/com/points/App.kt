package tinkoff.turlir.com.points

import android.app.Application
import tinkoff.turlir.com.points.di.AppComponent
import tinkoff.turlir.com.points.di.AppModule
import tinkoff.turlir.com.points.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}