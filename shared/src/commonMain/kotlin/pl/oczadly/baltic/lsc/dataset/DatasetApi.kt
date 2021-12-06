package pl.oczadly.baltic.lsc.dataset

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.headers
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem
import pl.oczadly.baltic.lsc.model.Response


class DatasetApi(private val userState: UserState) {

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

    suspend fun fetchDatasetShelf(): Response<DatasetShelfItem> {
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/task/datashelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }
}