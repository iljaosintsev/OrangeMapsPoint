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

    val tabComponent = object: DynamicComponent<TabsComponent> {

        private var link: TabsComponent? = null

        override fun open(): TabsComponent {
            if (link == null) {
                link = DaggerTabsComponent.builder()
                    .storageComponent(storageComponent)
                    .build()
            }

            return get()
        }

        override fun close() {
            link = null
        }

        override fun get(): TabsComponent {
            return link!!
        }
    }

    val pointComponent = object: DynamicComponent<PointComponent> {

        private var link: PointComponent? = null

        override fun open(): PointComponent {
            if (link == null) {
                link = DaggerPointComponent.builder()
                    .storageComponent(storageComponent)
                    .build()
            }

            return get()
        }

        override fun close() {
            link = null
        }

        override fun get(): PointComponent {
            return link!!
        }
    }

}