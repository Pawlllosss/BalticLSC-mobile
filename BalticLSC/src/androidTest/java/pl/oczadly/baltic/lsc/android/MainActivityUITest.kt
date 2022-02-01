package pl.oczadly.baltic.lsc.android

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import kotlinx.datetime.LocalDateTime
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.UserState
import okhttp3.mockwebserver.MockResponse

import okhttp3.mockwebserver.RecordedRequest
import pl.oczadly.baltic.lsc.app.dto.App
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem
import pl.oczadly.baltic.lsc.model.Response
import okhttp3.HttpUrl

import okhttp3.Interceptor
import okhttp3.Request
import java.net.InetAddress
import java.net.InetSocketAddress
import com.google.gson.JsonParseException

import com.google.gson.JsonDeserializationContext

import com.google.gson.JsonDeserializer

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
import java.lang.reflect.Type
import java.time.ZoneId
import java.time.ZonedDateTime
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

//    https://stackoverflow.com/questions/49884647/nullifying-or-overriding-api-calls-made-in-an-activity-under-an-espresso-test

//    @get:Rule
    var mainViewRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val server = MockWebServer()

//    private val mRequestInterceptor = Interceptor { chain ->
//        var request: Request = chain.request()
//        val address = InetSocketAddress(InetAddress.getLocalHost(), 8080)
//        val httpUrl: HttpUrl =
//            request.url.newBuilder().scheme("https://").host("balticlsc.iem.pw.edu.pl").port(443)
//                .build()
//        request = request.newBuilder()
//            .url(httpUrl)
//            .build()
//        chain.proceed(request)
//    }

    val dispatcher: Dispatcher = object : Dispatcher() {
        //        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            val gson = Gson()
            when (request.path) {
                "/backend/app/shelf/" -> {
                    return MockResponse().setResponseCode(200)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .setBody(
                            "{\"success\":true,\"message\":\"ok\",\"data\":[{\"diagramUid\":\"9f0fddf8-ac41-4db5-a5bc-5ff8c00f1bb8\",\"unit\":{\"name\":\"Edging Image Processor\",\"uid\":\"1f7a053a-ae08-48cf-ae13-ce3c5c9fa4dd\",\"pClass\":null,\"shortDescription\":\"Edges color images.\",\"longDescription\":\"Processes images by splitting into RGB and edging.\",\"keywords\":null,\"icon\":\"https://www.balticlsc.eu/model/_icons/yap_001.png\",\"isApp\":true,\"isService\":false},\"version\":\"0.1\",\"uid\":\"MarekImageProcessor2_rel_001\",\"status\":2,\"date\":\"2021-09-23T10:00:19.019816\",\"description\":\"First version of the processor\",\"openSource\":false,\"usageCounter\":0,\"pins\":[{\"uid\":\"fcd7411b-efae-49d7-9671-cc27ffe89e6f\",\"name\":\"InputImages\",\"binding\":0,\"tokenMultiplicity\":0,\"dataMultiplicity\":1,\"dataTypeUid\":\"dd-003-000\",\"dataTypeName\":\"ImageFile\",\"dataStructureUid\":null,\"dataStructureName\":null,\"accessTypeUid\":\"dd-006-000\",\"accessTypeName\":\"FTP\"},{\"uid\":\"2bbb7077-a955-438e-b4cc-e2d16139f05d\",\"name\":\"OutputImages\",\"binding\":2,\"tokenMultiplicity\":0,\"dataMultiplicity\":1,\"dataTypeUid\":\"dd-003-000\",\"dataTypeName\":\"ImageFile\",\"dataStructureUid\":null,\"dataStructureName\":null,\"accessTypeUid\":\"dd-006-000\",\"accessTypeName\":\"FTP\"}],\"supportedResourcesRange\":{\"minCPUs\":0,\"minGPUs\":0,\"minMemory\":0,\"minStorage\":0,\"maxCPUs\":0,\"maxGPUs\":0,\"maxMemory\":0,\"maxStorage\":0}}]}"
                        )
                }
                "/backend/app/list/" -> return MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\"success\":true,\"message\":\"ok\",\"data\":[{\"diagramUid\":\"0e200c07-08b1-42de-aa85-1165311b7f91\",\"releases\":[],\"name\":\"Added new app\",\"uid\":\"1f7a053a-ae08-48cf-ae13-ce3c5c9fa4dd\",\"pClass\":null,\"shortDescription\":null,\"longDescription\":null,\"keywords\":null,\"icon\":\"https://www.balticlsc.eu/model/_icons/default.png\",\"isApp\":true,\"isService\":false}]}")
            }
            return MockResponse().setResponseCode(404)
        }
    }

    @Before
    fun setup() {
        MainActivity.setUserState(UserState("test-access-token"))
//        val appShelfResponse: Response<AppShelfItem> = Response(
//            true,
//            "OK",
//            listOf(
//                AppShelfItem(
//                    "test",
//                    "test-diagram",
//                    App("test", "Test app name", "icon", "Short description"),
//                    "1.0",
//                    LocalDateTime(2021, 5, 2, 10, 0),
//                    listOf()
//                )
//            )
//        )

//            .setBody(gson.toJson(appShelfResponse)))
//        server.enqueue(MockResponse().setResponseCode(200)
//            .setBody(Gson().toJson(appShelfResponse)))
        server.start()

//        mock
//        server.start(InetAddress.getByName("localhost"), 443)
//        server.start(443)
        val baseUrl = server.url("backend/")
//        MainActivity.apiBasePath = baseUrl.toString()
        server.dispatcher = dispatcher
//        server.requireClientAuth()
    }

    @Test
    fun shouldDisplayAppListAfterLoading() {
        val baseUrl = server.url("")
        MainActivity.apiBasePath = baseUrl.host
        MainActivity.apiPort = baseUrl.port
        mainViewRule.launchActivity(null)

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("BalticLSC"))))


        onView(withId(R.id.app_store_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Added new app")))));

//        onView(withId(R.id.app_store_recycler_view))
//            .perform(
//                RecyclerViewActions.actionOnItem(
//                hasDescendant(withText("Text of item you want to scroll to")),
//                click()));
        val request1 = server.takeRequest()
    }

    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}