package pl.oczadly.baltic.lsc.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.android.view.login.LoginView

@RunWith(AndroidJUnit4::class)
@LargeTest
class SampleTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LoginView>()

    @Test
    fun changeText_sameActivity() {
        onView(withId(R.id.edit_text_email))
            .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard())

        onView(withId(R.id.edit_text_email)).check(matches(withText(STRING_TO_BE_TYPED)))
    }

    companion object {
        val STRING_TO_BE_TYPED = "Espresso"
    }
}