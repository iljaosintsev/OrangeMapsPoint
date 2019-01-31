package tinkoff.turlir.com.points.base

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointHolder @Inject constructor() {

    var point: String = ""
        set(value) {
            field = value
            for (listener in callbacks) {
                listener.onPointChange(value)
            }
        }

    private var callbacks = mutableListOf<OnPointChangedListener>()

    fun addOnPointChangedListener(callback: OnPointChangedListener) {
        callbacks.add(callback)
    }

    fun removeCallbacks() {
        callbacks.clear()
    }

    interface OnPointChangedListener {

        fun onPointChange(point: String)
    }
}