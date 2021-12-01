package pl.oczadly.baltic.lsc.computation

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import pl.oczadly.baltic.lsc.QueryParams
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.computation.dto.Task
import pl.oczadly.baltic.lsc.computation.dto.TaskCreate
import pl.oczadly.baltic.lsc.model.Response
import pl.oczadly.baltic.lsc.model.SingleResponse


class ComputationApi(private val userState: UserState) {

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

    suspend fun fetchComputationTasks(): Response<Task> {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/task/list/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = QueryParams("")
        }
    }

    suspend fun initiateComputationTask(task: TaskCreate, releaseUid: String): SingleResponse<String> {
//        https://balticlsc.iem.pw.edu.pl/backend/task/initiate?releaseUid=MarekImageProcessor2_rel_0
        return client.put("https://balticlsc.iem.pw.edu.pl/backend/task/initiate") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            parameter("releaseUid", releaseUid)
            body = QueryParams("")
        }
    }
}