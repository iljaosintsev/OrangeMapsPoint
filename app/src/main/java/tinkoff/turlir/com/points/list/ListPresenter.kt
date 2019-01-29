package tinkoff.turlir.com.points.list

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.storage.Repository
import javax.inject.Inject

@InjectViewState
class ListPresenter @Inject constructor(private val repo: Repository) :
    BasePresenter<ListPointsView>() {

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
            }, { error ->
                Log.e("ListPresenter", error.message)
                error.message?.let {
                    viewState.error(it)
                }
            })
    }
}
