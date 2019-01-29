package tinkoff.turlir.com.points.maps

import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.storage.Repository
import tinkoff.turlir.com.points.storage.SharedPrefs
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheValidator @Inject constructor(
    private val repo: Repository,
    private val prefs: SharedPrefs
) {

    fun validate(): Completable {
        val mark = prefs.time
        if (mark == null) {
            Log.d("CacheValidator", "cache time not found")
            return repo.cachePartner().ignoreElement()
                .doOnComplete {
                    prefs.time = Date()
                }
        } else {
            val now = Date()
            val diff = Math.abs(now.time - mark.time)
            if (diff > TimeUnit.MINUTES.toMillis(10)) {
                Log.d("CacheValidator", "cleaning cache")
                return clearCache()
                    .andThen(repo.cachePartner())
                    .ignoreElement()
                    .doOnComplete {
                        prefs.time = Date()
                    }
            } else {
                Log.d("CacheValidator", "cache valid")
                return Completable.complete()
            }
        }
    }

    private fun clearCache(): Completable {
        return repo.clear()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}