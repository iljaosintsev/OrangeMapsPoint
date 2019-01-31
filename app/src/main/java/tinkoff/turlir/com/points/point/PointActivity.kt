package tinkoff.turlir.com.points.point

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
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

    private var point: MapsPoint? = null
    private var partner: Partner? = null

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

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val point = point ?: return
        val partner = partner ?: return
        setContent(point, partner)
    }

    override fun renderPoint(point: MapsPoint, partner: Partner) {
        this.point = point
        this.partner = partner
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

        if (presenter.isInRestoreState(this)) {
            setContent(point, partner)
        }
    }

    private fun setContent(point: MapsPoint, partner: Partner) {
        point_partner_name.text = partner.name
        setTextField(point_id_hint, point_id, point.externalId)
        setTextField(point_ful_address_hint, point_address, point.fullAddress)
        setTextField(point_partner_hint, point_partner_desc, partner.description)
        setTextField(point_work_hint, point_hours, null)
        if (point.viewed) {
            point_viewed_hint.visibility = View.VISIBLE
        } else {
            point_viewed_hint.visibility = View.INVISIBLE
        }

        val set = TransitionSet().apply {
            addTransition(Fade())
            duration = 850
        }
        TransitionManager.beginDelayedTransition(point_root, set)
        point_content.visibility = View.VISIBLE
    }

    private fun setTextField(hint: TextView, content: TextView, text: String?) {
        content.text = text
        hint.visibility = if (text == null || text.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    override fun notFound(id: String) {
        supportStartPostponedEnterTransition()
        point_avatar.visibility = View.GONE
        point_content.visibility = View.GONE
        point_empty_stub.text = getString(R.string.point_not_found, id)
        point_empty_stub.visibility = View.VISIBLE
    }

    override fun error(message: String) {
        supportStartPostponedEnterTransition()
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
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