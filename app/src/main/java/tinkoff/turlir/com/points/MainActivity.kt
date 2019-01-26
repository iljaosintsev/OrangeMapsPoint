package tinkoff.turlir.com.points

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        act_main_pager.adapter = MapsListPager(supportFragmentManager)
        act_main_tabs.setupWithViewPager(act_main_pager)
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
}
