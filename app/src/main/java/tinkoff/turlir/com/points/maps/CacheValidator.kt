package tinkoff.turlir.com.points.maps

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
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

    var callback: Callback? = null

    fun check() {
        val mark = prefs.time
        if (mark == null) {
            Log.d("CacheValidator", "cache time not found")
            onCacheCleared()
        } else {
            val now = Date()
            val diff = Math.abs(now.time - mark.time)
            if (diff > TimeUnit.MINUTES.toMillis(10)) {
                Log.d("CacheValidator", "cleaning cache")
                clearCache()
            } else {
                Log.d("CacheValidator", "cache valid")
                onCacheValid()
            }
        }
    }

    private fun clearCache() {
        repo.clear()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableCompletableObserver() {
                override fun onComplete() {
                    dispose()
                    onCacheCleared()
                }

                override fun onError(e: Throwable) {
                    dispose()
                    onException(e)
                }
            })
    }

    private fun onCacheValid() {
        callback?.cacheValid()
    }

    private fun onCacheCleared() {
        prefs.time = Date()
        callback?.cacheCleared()
    }

    private fun onException(error: Throwable) {
        callback?.error(error)
    }

    interface Callback {

        fun cacheValid()

        fun cacheCleared()

        fun error(error: Throwable)
    }
}