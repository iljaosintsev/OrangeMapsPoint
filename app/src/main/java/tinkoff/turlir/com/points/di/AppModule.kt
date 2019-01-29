package tinkoff.turlir.com.points.di

import android.content.Context
import dagger.Module
import dagger.Provides
import tinkoff.turlir.com.points.base.DensitySaturation

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
}