package tinkoff.turlir.com.points

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_main.*
import tinkoff.turlir.com.points.list.ListFragment
import tinkoff.turlir.com.points.maps.MapsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.holder.tabComponent.open()
        setContentView(R.layout.activity_main)

        act_main_pager.adapter = MapsListPager(supportFragmentManager)
        act_main_tabs.setupWithViewPager(act_main_pager)
    }

    override fun onResume() {
        super.onResume()
        val availability = GoogleApiAvailability.getInstance()
        val available = availability.isGooglePlayServicesAvailable(this)
        if (available != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(available)) {
                Log.d(TAG, "show play services error dialog")
                val dialog = availability.getErrorDialog(this, available, PLAY_REQUEST)
                dialog.setOnDismissListener {
                    onResume()
                }
                dialog.show()
            } else {
                availability.showErrorNotification(this, available)
                blockInterface()
            }
        } else {
            openInterface()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.fragments[0]
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            App.holder.tabComponent.close()
            App.holder.pointComponent.close()
        }
    }

    private fun blockInterface() {
        Log.d(TAG, "block interface")
        act_main_pager.visibility = View.GONE
        act_main_tabs.visibility = View.GONE
        act_main_stub.visibility = View.VISIBLE
    }

    private fun openInterface() {
        Log.d(TAG, "open interface")
        act_main_pager.visibility = View.VISIBLE
        act_main_tabs.visibility = View.VISIBLE
        act_main_stub.visibility = View.GONE
    }

    inner class MapsListPager(sfm: FragmentManager) : FragmentPagerAdapter(sfm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MapsFragment()
                else -> ListFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.maps_title)
                else -> getString(R.string.list_title)
            }
        }

        override fun getCount() = 2
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PLAY_REQUEST = 24
    }
}
