package tinkoff.turlir.com.points.di

import android.content.Context
import com.google.android.gms.maps.model.CameraPosition
import dagger.Module
import dagger.Provides
import tinkoff.turlir.com.points.base.AsyncEvent
import tinkoff.turlir.com.points.base.BehaviorEvent
import tinkoff.turlir.com.points.base.DensitySaturation
import java.util.concurrent.TimeUnit

@Module
class AppModule(cnt: Context) {

    private val context = cnt.applicationContext

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideDensitySaturation(): DensitySaturation {
        return DensitySaturation()
    }

    @Provides
    fun provideAsyncEvent(): AsyncEvent<CameraPosition> {
        return BehaviorEvent(750, TimeUnit.MILLISECONDS)
    }
}