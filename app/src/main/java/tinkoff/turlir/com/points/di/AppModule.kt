package tinkoff.turlir.com.points.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(cnt: Context) {

    private val context = cnt.applicationContext

    @Provides
    fun provideContext(): Context {
        return context
    }
}