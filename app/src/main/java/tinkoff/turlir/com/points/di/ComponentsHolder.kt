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

    val tabComponent = object: AbstractDynamicComponent<TabsComponent>() {

        override fun openScope(): TabsComponent {
            return DaggerTabsComponent.builder()
                .storageComponent(storageComponent)
                .build()
        }
    }

    val pointComponent = object: AbstractDynamicComponent<PointComponent>() {

        override fun openScope(): PointComponent {
            return DaggerPointComponent.builder()
                .storageComponent(storageComponent)
                .build()
        }
    }

}