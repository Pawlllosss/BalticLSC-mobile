package pl.oczadly.baltic.lsc.android.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.computation.activity.ComputationView
import pl.oczadly.baltic.lsc.android.view.dataset.activity.DatasetView

class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AppStoreView()
            }
            1 -> {
                ComputationView()
            }
            2 -> {
                DatasetView()
            }
            else -> {
                return DefaultView()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Apps"
            1 -> "Compute"
            2 -> "Data"
            else -> {
                return "Other"
            }
        }
    }
}