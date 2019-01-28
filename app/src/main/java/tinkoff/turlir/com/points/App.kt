package tinkoff.turlir.com.points

import android.app.Application
import tinkoff.turlir.com.points.di.ComponentsHolder

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        holder = ComponentsHolder(this)
    }

    companion object {
        lateinit var holder: ComponentsHolder
    }
}