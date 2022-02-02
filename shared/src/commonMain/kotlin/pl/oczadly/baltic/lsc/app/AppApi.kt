package pl.oczadly.baltic.lsc.app

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.host
import io.ktor.client.request.parameter
import io.ktor.client.request.port
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.URLProtocol
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.QueryParams
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.app.dto.AppEdit
import pl.oczadly.baltic.lsc.app.dto.AppReleaseEdit
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem
import pl.oczadly.baltic.lsc.model.NoDataResponse
import pl.oczadly.baltic.lsc.model.Response
import pl.oczadly.baltic.lsc.model.SingleResponse


class AppApi(private val apiConfig: ApiConfig, private val userState: UserState) {

    private val client = HttpClient {
        defaultRequest {
            host = apiConfig.host
            port = apiConfig.port
            url {
                protocol = if (apiConfig.https) URLProtocol.HTTPS else URLProtocol.HTTP
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    suspend fun fetchApplicationShelf(): Response<AppShelfItem> {
        return client.get("backend/app/shelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun fetchApplicationList(): Response<AppListItem> {
        return client.post("backend/app/list/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = QueryParams("")
        }
    }

    suspend fun fetchApplicationListItemByUid(appUid: String): SingleResponse<AppListItem> {
        return client.get("backend/app") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("appUid", appUid)
        }
    }

    suspend fun createAppRelease(releaseName: String, appUid: String): NoDataResponse {
        return client.put("backend/dev/appRelease") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("version", releaseName)
            parameter("appUid", appUid)
        }
    }

    suspend fun deleteAppRelease(releaseUid: String): NoDataResponse {
        return client.delete("backend/dev/release") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun editAppRelease(appReleaseEdit: AppReleaseEdit): NoDataResponse {
        return client.post("backend/dev/appRelease") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = appReleaseEdit
        }
    }

    suspend fun addReleaseToCockpit(releaseUid: String): NoDataResponse {
        return client.post("backend/app/shelf") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun deleteReleaseFromCockpit(releaseUid: String): NoDataResponse {
        return client.delete("backend/app/shelf") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun createApp(appName: String): NoDataResponse {
        return client.put("backend/dev/app") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("name", appName)
        }
    }

    suspend fun editApp(appEditDTO: AppEdit): NoDataResponse {
        return client.post("backend/dev/unit") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = appEditDTO
        }
    }

    suspend fun deleteApp(appUid: String): NoDataResponse {
        return client.delete("backend/dev/unit") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("unitUid", appUid)
        }
    }
}
