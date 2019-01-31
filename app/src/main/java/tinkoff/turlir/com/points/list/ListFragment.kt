package tinkoff.turlir.com.points.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*
import tinkoff.turlir.com.points.App
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.base.MvpFragment
import tinkoff.turlir.com.points.point.PointActivity
import tinkoff.turlir.com.points.storage.PointPicturable

class ListFragment: MvpFragment(), ListPointsView, PointsAdapter.PointClickCallback {

    @InjectPresenter
    lateinit var presenter: ListPresenter

    @ProvidePresenter
    fun provideListPresenter(): ListPresenter {
        return App.holder.tabComponent.get().listPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, parent, false)
    }

    private lateinit var adapter: PointsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dpi = App.holder.storageComponent.dpiProvider().get()
        adapter = PointsAdapter(dpi, this)
        list_recycler.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        list_recycler.adapter = adapter
        val dividers = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        list_recycler.addItemDecoration(dividers)
        list_recycler.setHasFixedSize(true)
    }

    override fun presentPoints(points: List<PointPicturable>) {
        list_recycler.visibility = View.VISIBLE
        list_empty_stub.visibility = View.GONE
        adapter.replace(points)
    }

    override fun empty() {
        list_recycler.visibility = View.GONE
        list_empty_stub.visibility = View.VISIBLE
    }

    override fun error(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    override fun openPoint(adapterPosition: Int, point: PointPicturable, avatar: ImageView) {
        val moveKey = point.point.externalId
        ViewCompat.setTransitionName(avatar, moveKey)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity!!,
            avatar,
            moveKey
        )
        val intent = PointActivity.newIntent(moveKey, requireContext())
        startActivity(intent, options.toBundle())
    }
}