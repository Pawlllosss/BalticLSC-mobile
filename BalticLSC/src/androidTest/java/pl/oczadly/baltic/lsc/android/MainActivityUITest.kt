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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.android.utils.atPosition


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

    var mainViewRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val server = MockWebServer()

    val dispatcher: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
//            this.javaClass.classLoader.getResourceAsStream("test2.keystore.bks") way to retrieve file from resources
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
        server.start()
        server.dispatcher = dispatcher
    }

    @Test
    fun shouldDisplayAppListAfterLoading() {
        val baseUrl = server.url("")
        MainActivity.apiConfig = ApiConfig(baseUrl.host, baseUrl.port, false)
        mainViewRule.launchActivity(null)

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("BalticLSC"))))

        onView(withId(R.id.app_store_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Added new app (Owned)")))));


        val request1 = server.takeRequest()
    }
}