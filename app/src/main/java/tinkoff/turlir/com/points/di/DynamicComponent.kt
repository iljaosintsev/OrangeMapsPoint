package tinkoff.turlir.com.points.di

interface DynamicComponent<T> {

    fun open(): T

    fun close()

    fun get(): T
}