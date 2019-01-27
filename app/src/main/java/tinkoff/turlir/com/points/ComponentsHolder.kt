package tinkoff.turlir.com.points

import android.content.Context
import tinkoff.turlir.com.points.di.*

class ComponentsHolder constructor(context: Context) {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(context))
        .build()

    lateinit var tabComponent: TabsComponent

    fun memberTabs() {
        if (!::tabComponent.isInitialized) {
            recreateTabs()
        }
    }

    fun recreateTabs(): TabsComponent {
        return DaggerTabsComponent.builder()
            .appComponent(appComponent)
            .build()
            .also {
                tabComponent = it
            }
    }

}