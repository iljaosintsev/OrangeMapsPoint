package tinkoff.turlir.com.points.di

import android.content.Context
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides

@Module
class TabModule {

    @Provides
    @TabScope
    fun provideRxLocation(cnt: Context): RxLocation {
        return RxLocation(cnt)
    }
}
