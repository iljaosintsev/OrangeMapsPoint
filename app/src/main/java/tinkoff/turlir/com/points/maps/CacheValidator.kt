package tinkoff.turlir.com.points.maps

import android.util.Log
import io.reactivex.Completable
import tinkoff.turlir.com.points.di.TabScope
import tinkoff.turlir.com.points.storage.Repository
import tinkoff.turlir.com.points.storage.SharedPrefs
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@TabScope
class CacheValidator @Inject constructor(
    private val repo: Repository,
    private val prefs: SharedPrefs
) {

    fun validate(base: Date = Date()): Completable {
        val mark = prefs.time
        if (mark != null && isOver(mark, base)) {
            Log.d("CacheValidator", "cleaning cache")
            return repo.clear()
                .andThen(repo.cachePartner())
                .ignoreElement()
                .doOnComplete {
                    prefs.time = Date()
                }

        } else if (mark == null) {
            Log.d("CacheValidator", "cache time not found")
            return repo.cachePartner().ignoreElement()
                .doOnComplete {
                    prefs.time = Date()
                }

        } else {
            Log.d("CacheValidator", "cache valid")
            return Completable.complete()
        }
    }

    private fun isOver(mark: Date, base: Date): Boolean {
        val diff = Math.abs(mark.time - base.time)
        return diff > TimeUnit.MINUTES.toMillis(10)
    }
}