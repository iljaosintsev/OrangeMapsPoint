package tinkoff.turlir.com.points.maps

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionChecker @Inject constructor(private val cnt: Context) {

    fun check(permission: String, callback: Callback) {
        val result = ContextCompat.checkSelfPermission(cnt, permission)
        if (result == PackageManager.PERMISSION_GRANTED) {
            callback.granted()
        } else {
            callback.denied()
        }
    }

    interface Callback {

        fun granted()

        fun denied()
    }
}