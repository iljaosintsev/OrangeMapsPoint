package tinkoff.turlir.com.points.point

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.storage.Repository
import javax.inject.Inject

@InjectViewState
class PointPresenter @Inject constructor(private val repo: Repository): BasePresenter<PointView>() {

    var id: String = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.v("PointPresenter", "id = $id")

        disposed + repo.pointById(id)
            .flatMap({ point ->
                repo.partnerById(point.partnerName)
            }, { point, partner ->
                point to partner
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (point, partner) ->
                setViewed(point.externalId)
                viewState.renderPoint(point, partner)
            }, { error ->
                Log.e("MapsPresenter", error.message)
                error.message?.let {
                    viewState.error(it)
                }
            }, {
                Log.w("MapsPresenter", "point or partner not found, $id")
                viewState.notFound()
            })

    }

    private fun setViewed(id: String) {
        disposed + repo.setPointViewed(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.v("MapsPresenter", "set point $id viewed")
            }, { error ->
                Log.e("MapsPresenter", error.message)
            })
    }
}