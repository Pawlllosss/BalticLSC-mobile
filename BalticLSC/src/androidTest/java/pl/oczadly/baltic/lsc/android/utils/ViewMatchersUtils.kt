package pl.oczadly.baltic.lsc.android.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun matchRecyclerViewItemAtPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("Has list item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}

fun matchTabTitleAtPosition(tabTitle: String, tabIndex: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Unable to select tab at index $tabIndex with title $tabTitle")
        }

        override fun matchesSafely(item: View?): Boolean {
            val tabLayout = item as TabLayout
            val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex) ?: return false

            return tabAtIndex.text.toString().contains(tabTitle, true)
        }
    }
}
