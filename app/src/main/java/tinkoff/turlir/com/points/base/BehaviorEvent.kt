package tinkoff.turlir.com.points.base

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class BehaviorEvent<T>(
    private val time: Long,
    private val unit: TimeUnit,
    threadSafe: Boolean = false
) : AsyncEvent<T> {

    private val subject: Subject<T> = if (!threadSafe) {
        BehaviorSubject.create<T>()
    } else {
        BehaviorSubject.create<T>().toSerialized()
    }

    override fun push(event: T) {
        subject.onNext(event)
    }

    override fun observe(): Observable<T> {
        return subject
            .debounce(time, unit)
    }
}