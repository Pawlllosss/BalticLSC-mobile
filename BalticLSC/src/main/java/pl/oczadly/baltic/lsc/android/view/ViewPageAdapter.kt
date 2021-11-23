package pl.oczadly.baltic.lsc.android.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.oczadly.baltic.lsc.android.view.app.AppStoreView
import pl.oczadly.baltic.lsc.android.view.computation.ComputationView

class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AppStoreView()
            }
            1 -> {
                ComputationView()
            }
            else -> {
                return DefaultView()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "App Store"
            1 -> "Computation cockpit"
            2 -> "Data shelf"
            3 -> "Development shelf"
            else -> {
                return "Other"
            }
        }
    }
}