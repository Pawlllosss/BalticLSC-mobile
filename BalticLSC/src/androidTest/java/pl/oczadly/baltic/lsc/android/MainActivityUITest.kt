package pl.oczadly.baltic.lsc.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
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
import org.hamcrest.CoreMatchers.anyOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.android.utils.atPosition
import java.io.BufferedReader


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
    fun shouldDisplayAppListAfterLoading() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("BalticLSC"))))
        onView(withId(R.id.app_store_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        allOf(
                            hasDescendant((withText("Test 1 app (Owned)"))),
                            hasDescendant(withText("Updated on 2021-09-23 10:00:19")),
                            hasDescendant(withText("Edges color images."))
                        )
                    )
                )
            )
    }
}