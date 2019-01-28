package tinkoff.turlir.com.points.storage

abstract class Container<T> {
    abstract val payload: List<T>
}