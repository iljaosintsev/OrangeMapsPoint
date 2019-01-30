package tinkoff.turlir.com.points.di

import android.util.Log

abstract class AbstractDynamicComponent<T>: DynamicComponent<T> {

    private var link: T? = null

    abstract fun openScope(): T

    override fun open(): T {
        if (link == null) {
            link = openScope()
        }
        return get()
    }

    override fun get(): T {
        if (link == null) {
            throw IllegalStateException("scope closed")
        }
        return link!!
    }

    override fun close() {
        if (link == null) {
            Log.w("DI", "scope already closed")
        }
        link = null
    }

    fun present() = link
}