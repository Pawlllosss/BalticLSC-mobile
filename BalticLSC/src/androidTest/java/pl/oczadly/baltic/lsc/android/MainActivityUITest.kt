package pl.oczadly.baltic.lsc.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.android.utils.matchRecyclerViewItemAtPosition
import pl.oczadly.baltic.lsc.android.utils.matchTabTitleAtPosition
import java.io.BufferedReader
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

    var mainViewRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val server = MockWebServer()

    val dispatcher: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            when (request.path) {
                "/backend/app/shelf/" -> {
                    return MockResponse().setResponseCode(200)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .setBody(getResponseFromResource("AppShelfResponse.json"))
                }
                "/backend/app/list/" -> return MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody(getResponseFromResource("AppListResponse.json"))
            }
            return MockResponse().setResponseCode(404)
        }

        private fun getResponseFromResource(fileName: String): String {
            val appShelfResponse =
                this.javaClass.classLoader.getResourceAsStream(fileName)
            val response = appShelfResponse.bufferedReader().use(BufferedReader::readText)
            return response
        }
    }

    @Before
    fun setup() {
        MainActivity.setUserState(UserState("test-access-token"))
        server.start()
        server.dispatcher = dispatcher
        val baseUrl = server.url("")
        MainActivity.apiConfig = ApiConfig(baseUrl.host, baseUrl.port, false)
        mainViewRule.launchActivity(null)
    }

    @Test
    fun shouldDisplayToolbarAndAppListAfterLoading() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("BalticLSC"))))
        onView(withId(R.id.tab_layout)).check(
            matches(
                allOf(
                    matchTabTitleAtPosition("APPS", 0),
                    matchTabTitleAtPosition("COMPUTE", 1),
                    matchTabTitleAtPosition("DATA", 2)
                )
            )
        )
        onView(withId(R.id.app_store_add_app_button)).check(matches(isDisplayed()))
        sleep(500)

        onView(withId(R.id.app_store_recycler_view))
            .check(
                matches(
                    matchRecyclerViewItemAtPosition(
                        0,
                        getAppListItemMatchers(
                            "Test 1 app (Owned)",
                            "2021-09-23 10:00:19",
                            "Edges color images."
                        )
                    )
                )
            )

        onView(withId(R.id.app_store_recycler_view))
            .check(
                matches(
                    matchRecyclerViewItemAtPosition(
                        1,
                        getAppListItemMatchers(
                            "Covid-2 Analyzer",
                            "2021-09-23 10:00:19",
                            "Analysis of Covid-2"
                        )
                    )
                )
            )

        onView(withId(R.id.app_store_recycler_view)).perform(swipeUp())
        onView(withId(R.id.app_store_recycler_view))
            .check(
                matches(
                    matchRecyclerViewItemAtPosition(
                        2,
                        getAppListItemMatchers(
                            "Distance Matrix Calculator (Can be added)",
                            "2022-01-02 20:26:12",
                            "Finds all distances between given addresses using user-provided map"
                        )
                    )
                )
            )
        onView(withId(R.id.app_store_add_app_button)).check(matches(isDisplayed()))
    }

    private fun getAppListItemMatchers(name: String, date: String, description: String) = allOf(
        hasDescendant((withText(name))),
        hasDescendant(withText("Updated on $date")),
        hasDescendant(withText(description))
    )
}