package tinkoff.turlir.com.points.point

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tinkoff.turlir.com.points.base.BasePresenter
import tinkoff.turlir.com.points.base.PointHolder
import tinkoff.turlir.com.points.storage.Repository
import javax.inject.Inject

@InjectViewState
class PointPresenter @Inject constructor(
    private val repo: Repository,
    private val pointHolder: PointHolder
) : BasePresenter<PointView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val id = pointHolder.point
        Log.v("PointPresenter", "id = $id")

        disposed + repo.isPointViewed(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.viewed(it)
                if (!it) {
                    setViewed(id)
                }
            }, ::handleError, {
                Log.w("MapsPresenter", "point or partner not found, $id")
            })
    }

    private fun setViewed(id: String) {
        disposed + repo.setPointViewed(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.v("MapsPresenter", "set point $id viewed")
            }, ::handleError)
    }
}