package tinkoff.turlir.com.points.point

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_point.*
import tinkoff.turlir.com.points.App
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.base.MvpActivity
import tinkoff.turlir.com.points.maps.MapsPoint
import tinkoff.turlir.com.points.storage.Partner

class PointActivity: MvpActivity(), PointView {

    @InjectPresenter
    lateinit var presenter: PointPresenter

    private val id: String
        get() = intent!!.getStringExtra(ARG_POINT)

    private val dpi: String by lazy(LazyThreadSafetyMode.NONE) {
        App.holder.storageComponent.dpiProvider().get()
    }

    @ProvidePresenter
    fun providePointPresenter(): PointPresenter {
        return App.holder.tabComponent.pointPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.id = id
        supportPostponeEnterTransition()
        setContentView(R.layout.activity_point)
        point_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun renderPoint(point: MapsPoint, partner: Partner) {
        found()
        Picasso.with(this)
            .load(partner.picture(dpi))
            .fit()
            .centerCrop()
            .into(point_avatar, object: Callback {
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }

                override fun onError() {
                    supportStartPostponedEnterTransition()
                }
            })

        point_address.text = point.fullAddress
        point_partner.text = point.partnerName
        point_hours.text = point.workHours
        point_viewed_hint.visibility = if (point.viewed) View.VISIBLE else View.GONE
    }

    override fun notFound() {
        point_content_group.visibility = View.GONE
        point_viewed_hint.visibility = View.GONE
        point_empty_stub.text = getString(R.string.point_not_found, id)
        point_empty_stub.visibility = View.VISIBLE
    }

    override fun error(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun found() {
        point_content_group.visibility = View.VISIBLE
        point_empty_stub.visibility = View.GONE
    }

    companion object {

        private const val ARG_POINT = "arg_point"

        fun newIntent(id: String, cnt: Context): Intent {
            return Intent(cnt, PointActivity::class.java).apply {
                putExtra(ARG_POINT, id)
            }
        }

    }
}