package tinkoff.turlir.com.points.point

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
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

    private val transitionName: String
        get() = intent!!.getStringExtra(ARG_TRANSITION)

    private val dpi: String by lazy(LazyThreadSafetyMode.NONE) {
        App.holder.storageComponent.dpiProvider().get()
    }

    @ProvidePresenter
    fun providePointPresenter(): PointPresenter {
        return App.holder.pointComponent.open().pointPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.id = id
        supportPostponeEnterTransition()
        setContentView(R.layout.activity_point)
        if (savedInstanceState == null) {
            ViewCompat.setTransitionName(point_avatar, transitionName)
        }
        point_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        point_viewed_hint.animate().cancel()
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

        point_partner_name.text = partner.name
        point_id.text = point.externalId
        point_address.text = point.fullAddress
        point_partner_desc.text = partner.description
        point_hours.text = point.workHours
        if (point.viewed) {
            point_viewed_hint.apply {
                visibility = View.VISIBLE
                alpha = 0f
                animate()
                    .alpha(1f)
                    .setStartDelay(950)
                    .setDuration(750)
                    .start()
            }
        } else {
            point_viewed_hint.visibility = View.INVISIBLE
        }
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
        private const val ARG_TRANSITION = "arg_transition"

        fun newIntent(id: String, transitionKey: String, cnt: Context): Intent {
            return Intent(cnt, PointActivity::class.java).apply {
                putExtra(ARG_POINT, id)
                putExtra(ARG_TRANSITION, transitionKey)
            }
        }
    }
}