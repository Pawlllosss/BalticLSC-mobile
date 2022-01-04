package pl.oczadly.baltic.lsc.app

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import pl.oczadly.baltic.lsc.QueryParams
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.app.dto.AppEdit
import pl.oczadly.baltic.lsc.app.dto.AppReleaseEdit
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem
import pl.oczadly.baltic.lsc.app.dto.list.AppListItem
import pl.oczadly.baltic.lsc.model.NoDataResponse
import pl.oczadly.baltic.lsc.model.Response


class AppApi(private val userState: UserState) {

    private val client = HttpClient {
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
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/app/shelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun fetchApplicationList(): Response<AppListItem> {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/app/list/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = QueryParams("")
        }
    }

    suspend fun createAppRelease(releaseName: String, appUid: String): NoDataResponse {
        return client.put("https://balticlsc.iem.pw.edu.pl/backend/dev/appRelease") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("version", releaseName)
            parameter("appUid", appUid)
        }
    }

    suspend fun deleteAppRelease(releaseUid: String): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/dev/release") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun editAppRelease(appReleaseEdit: AppReleaseEdit): NoDataResponse {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/dev/appRelease") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = appReleaseEdit
        }
    }

    suspend fun addReleaseToCockpit(releaseUid: String): NoDataResponse {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/app/shelf") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun deleteReleaseFromCockpit(releaseUid: String): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/app/shelf") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("releaseUid", releaseUid)
        }
    }

    suspend fun editApp(appEditDTO: AppEdit): NoDataResponse {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/dev/unit") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = appEditDTO
        }
    }


    suspend fun deleteApp(appUid: String): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/dev/unit") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("unitUid", appUid)
        }
    }
}
