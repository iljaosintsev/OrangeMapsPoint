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

    private val transitionIcon: String
        get() = intent!!.getStringExtra(ARG_TRANSITION_ICON)

    private val transitionTitle: String
        get() = intent!!.getStringExtra(ARG_TRANSITION_TITLE)

    private val dpi: String by lazy(LazyThreadSafetyMode.NONE) {
        App.holder.storageComponent.dpiProvider().get()
    }

    private val point: MapsPoint
        get() = intent!!.getParcelableExtra(ARG_POINT)

    private val partner: Partner
        get() = intent!!.getParcelableExtra(ARG_PARTNER)

    @ProvidePresenter
    fun providePointPresenter(): PointPresenter {
        return App.holder.pointComponent.open().pointPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)
        if (savedInstanceState == null) {
            ViewCompat.setTransitionName(point_avatar, transitionIcon)
            ViewCompat.setTransitionName(point_partner_name, transitionTitle)
        }
        supportPostponeEnterTransition()
        point_toolbar.setNavigationOnClickListener {
            point_content.visibility = View.INVISIBLE
            finishAfterTransition()
        }

        if (savedInstanceState == null) {
            setTransitionContent(point, partner)
        } else {
            setTransitionContent(point, partner)
            setContent(point, partner)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        point_content.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        point_viewed_hint.animate().cancel()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        setContent(point, partner)
    }

    override fun viewed(flag: Boolean) {
        point_viewed_hint.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    private fun setTransitionContent(point: MapsPoint, partner: Partner) {
        point_partner_name.text = point.partnerName
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

    private fun setContent(point: MapsPoint, partner: Partner) {
        setTextField(point_id_hint, point_id, point.externalId)
        setTextField(point_ful_address_hint, point_address, point.fullAddress)
        setTextField(point_partner_hint, point_partner_desc, partner.description)
        setTextField(point_work_hint, point_hours, null)

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

    override fun error(message: String) {
        supportStartPostponedEnterTransition()
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {

        private const val ARG_TRANSITION_ICON = "arg_transition_icon"
        private const val ARG_TRANSITION_TITLE = "arg_transition_title"
        private const val ARG_POINT = "arg_point"
        private const val ARG_PARTNER = "arg_partner"

        fun newIntent(
            transitionIcon: String,
            transitionTitle: String,
            point: MapsPoint,
            partner: Partner,
            cnt: Context
        ) = Intent(cnt, PointActivity::class.java).apply {
            putExtra(ARG_TRANSITION_ICON, transitionIcon)
            putExtra(ARG_TRANSITION_TITLE, transitionTitle)
            putExtra(ARG_POINT, point)
            putExtra(ARG_PARTNER, partner)
        }
    }
}