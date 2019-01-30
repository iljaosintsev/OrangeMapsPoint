package tinkoff.turlir.com.points.di

import android.content.Context

class ComponentsHolder constructor(context: Context) {

    private val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(context))
        .build()

    val storageComponent: StorageComponent = DaggerStorageComponent.builder()
        .appComponent(appComponent)
        .storageModule(StorageModule())
        .build()

    lateinit var tabComponent: TabsComponent

    lateinit var pointComponent: PointComponent

    fun memberTabs() {
        if (!::tabComponent.isInitialized) {
            recreateTabs()
        }
    }

    fun recreateTabs(): TabsComponent {
        return DaggerTabsComponent.builder()
            .storageComponent(storageComponent)
            .build()
            .also {
                tabComponent = it
            }
    }

    fun memberPoint() {
        if (!::pointComponent.isInitialized) {
            recreatePoint()
        }
    }

    fun recreatePoint(): PointComponent {
        return DaggerPointComponent.builder()
            .storageComponent(storageComponent)
            .build()
            .also {
                pointComponent = it
            }
    }

}