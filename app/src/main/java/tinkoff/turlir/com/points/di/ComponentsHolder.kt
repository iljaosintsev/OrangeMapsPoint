package tinkoff.turlir.com.points.di

import android.content.Context

class ComponentsHolder constructor(context: Context) {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(context))
        .build()

    val storageComponent = DaggerStorageComponent.builder()
        .appComponent(appComponent)
        .storageModule(StorageModule())
        .build()

    lateinit var tabComponent: TabsComponent

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

}