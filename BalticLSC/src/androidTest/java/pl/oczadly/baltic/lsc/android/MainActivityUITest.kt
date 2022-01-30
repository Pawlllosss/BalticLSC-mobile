package pl.oczadly.baltic.lsc.android

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.UserState
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

import com.github.tomakehurst.wiremock.junit.WireMockRule

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

    @get:Rule
    var mainViewRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var wireMockRule = WireMockRule(wireMockConfig().port(8080))

    @Before
    fun setup() {
        MainActivity.setUserState(UserState("test-access-token"))
    }

    @Test
    fun shouldDisplayAppListAfterLoading() {

    }
}