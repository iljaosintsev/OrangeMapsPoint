package tinkoff.turlir.com.points.base

import android.content.Context
import javax.inject.Inject
import javax.inject.Provider

class DpiProvider @Inject constructor(
    private val writer: DensityWriter,
    private val saturation: DensitySaturation,
    cnt: Context
) : Provider<String> {

    private val resources = cnt.applicationContext.resources

    override fun get(): String {
        return writer.apply(
            saturation.apply(resources.displayMetrics.densityDpi)
        )
    }
}