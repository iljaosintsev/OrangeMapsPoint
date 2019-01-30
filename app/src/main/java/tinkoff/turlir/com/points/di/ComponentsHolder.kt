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

    private var tabComponent: TabsComponent? = null

    private var pointComponent: PointComponent? = null

    //

    fun tabComponent(): TabsComponent {
        return tabComponent!!
    }

    fun openTab() {
        tabComponent = DaggerTabsComponent.builder()
            .storageComponent(storageComponent)
            .build()
    }

     fun closeTab() {
        tabComponent = null
    }

    //

    fun pointComponent(): PointComponent{
        return pointComponent!!
    }

    fun openPoint() {
        pointComponent = DaggerPointComponent.builder()
            .storageComponent(storageComponent)
            .build()
    }

    fun closePoint() {
        pointComponent = null
    }

}