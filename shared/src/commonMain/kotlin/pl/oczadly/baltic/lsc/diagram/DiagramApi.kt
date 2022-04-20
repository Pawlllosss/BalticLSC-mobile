package pl.oczadly.baltic.lsc.diagram

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.http.URLProtocol
import pl.oczadly.baltic.lsc.ApiConfig
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.diagram.dto.Diagram
import pl.oczadly.baltic.lsc.model.SingleResponse


class DiagramApi(private val apiConfig: ApiConfig, private val userState: UserState) {

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

    suspend fun fetchDiagramByReleaseUid(releaseUid: String): SingleResponse<Diagram> {
        return client.get("backend/editor/diagram/${releaseUid}") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }
}
