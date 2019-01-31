package tinkoff.turlir.com.points.point

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
        setContentView(R.layout.activity_point)
        if (savedInstanceState == null) {
            ViewCompat.setTransitionName(point_avatar, transitionName)
        }
        supportPostponeEnterTransition()
        point_toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    override fun onStop() {
        super.onStop()
        point_viewed_hint.animate().cancel()
    }

    override fun renderPoint(point: MapsPoint, partner: Partner) {
        point_partner_name.text = partner.name
        setTextField(point_id_hint, point_id, point.externalId)
        setTextField(point_ful_address_hint, point_address, point.fullAddress)
        setTextField(point_partner_hint, point_partner_desc, partner.description)
        setTextField(point_work_hint, point_hours, null)
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

        Picasso.with(this)
            .load(point.picture(partner.picture, dpi))
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
    }

    private fun setTextField(hint: TextView, content: TextView, text: String?) {
        content.text = text
        hint.visibility = if (text == null || text.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    override fun notFound(id: String) {
        supportStartPostponedEnterTransition()
        contentVisibility(View.GONE)
        point_viewed_hint.visibility = View.GONE
        point_empty_stub.text = getString(R.string.point_not_found, id)
        point_empty_stub.visibility = View.VISIBLE
    }

    override fun error(message: String) {
        supportStartPostponedEnterTransition()
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun contentVisibility(value: Int) {
        val views = listOf(
            point_partner_hint,
            point_work_hint,
            point_id,
            point_hours,
            point_address,
            point_partner_desc,
            point_id_hint,
            point_avatar,
            point_partner_name,
            point_ful_address_hint
        )
        for (view in views) {
            view.visibility = value
        }
    }

    companion object {

        private const val ARG_TRANSITION = "arg_transition"

        fun newIntent(transitionKey: String, cnt: Context): Intent {
            return Intent(cnt, PointActivity::class.java).apply {
                putExtra(ARG_TRANSITION, transitionKey)
            }
        }
    }
}