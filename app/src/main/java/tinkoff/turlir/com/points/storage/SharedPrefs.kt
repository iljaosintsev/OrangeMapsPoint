package tinkoff.turlir.com.points.storage

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SharedPrefs @Inject constructor(cnt: Context) {

    private val prefs = cnt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("ru", "RU")).also {
        it.timeZone = TimeZone.getTimeZone("UTC")
    }

    var time: Date?
        get() {
            val time = prefs.getString(TIME, null)
            return if (time != null) sdf.parse(time) else null
        }
        set(value) {
            val time = sdf.format(value)
            prefs.edit().putString(TIME, time).apply()
        }

    companion object {
        private const val PREF_NAME = "pref_name"
        private const val TIME = "time"
    }
}