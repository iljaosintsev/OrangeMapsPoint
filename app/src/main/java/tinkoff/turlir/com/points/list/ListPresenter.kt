package tinkoff.turlir.com.points.list

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.base.PointHolder
import tinkoff.turlir.com.points.di.TabScope
import tinkoff.turlir.com.points.maps.MapsPoint
import tinkoff.turlir.com.points.storage.Repository
import javax.inject.Inject

@InjectViewState
@TabScope
class ListPresenter @Inject constructor(
    private val repo: Repository,
    private val pointHolder: PointHolder
) : BasePresenter<ListPointsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        disposed + repo.allPoints()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next ->
                if (next.isNotEmpty()) {
                    Log.v("ListPresenter", "present ${next.size} items")
                    viewState.presentPoints(next)
                } else {
                    Log.d("ListPresenter", "present empty list")
                    viewState.empty()
                }
            }, ::handleError)
    }

    fun selectPoint(point: MapsPoint) {
        pointHolder.point = point.externalId
    }
}
