package tinkoff.turlir.com.points

import io.reactivex.Observable

interface AsyncEvent<T> {

    fun push(event: T)

    fun observe(): Observable<T>
}
