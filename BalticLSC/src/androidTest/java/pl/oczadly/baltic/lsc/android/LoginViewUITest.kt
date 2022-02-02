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
class LoginViewUITest {

    companion object {
        const val DEFAULT_LOGIN = "demo"
        const val DEFAULT_PASSWORD = "BalticDemo"
        const val STRING_TO_BE_TYPED = "Modified"
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LoginView>()

    @Test
    fun loginAndPasswordEditFieldsShouldBeVisibleAndEditableWhenEnteringText() {
        onView(withId(R.id.edit_text_email)).check(matches(withText(DEFAULT_LOGIN)))
        onView(withId(R.id.edit_text_password)).check(matches(withText(DEFAULT_PASSWORD)))

        onView(withId(R.id.edit_text_email))
            .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard())
        onView(withId(R.id.edit_text_password))
            .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard())

        onView(withId(R.id.edit_text_email)).check(matches(withText("$DEFAULT_LOGIN$STRING_TO_BE_TYPED")))
        onView(withId(R.id.edit_text_password)).check(matches(withText("$DEFAULT_PASSWORD$STRING_TO_BE_TYPED")))
    }
}