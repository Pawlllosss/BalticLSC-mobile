package pl.oczadly.baltic.lsc.computation

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import pl.oczadly.baltic.lsc.QueryParams
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.computation.dto.Task
import pl.oczadly.baltic.lsc.computation.dto.TaskCreate
import pl.oczadly.baltic.lsc.model.NoDataResponse
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

    suspend fun initiateComputationTask(
        task: TaskCreate,
        releaseUid: String
    ): SingleResponse<String> {
        return client.put("https://balticlsc.iem.pw.edu.pl/backend/task/initiate") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            parameter("releaseUid", releaseUid)
            body = task
        }
    }

    suspend fun abortComputationTask(taskUid: String): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/task/abort") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("taskUid", taskUid)
        }

    }

    //    https://balticlsc.iem.pw.edu.pl/backend/task/injectData?taskUid=0bbe1d61-f95e-4c04-a373-14704fd3730b&pinUid=ac1e0920-df2a-4b31-8adc-bc013a49ecdd&dataSetUid=f2b90e20-0eb0-43da-afc3-0524937ada21
//    https://balticlsc.iem.pw.edu.pl/backend/task/injectData?taskUid=0bbe1d61-f95e-4c04-a373-14704fd3730b&pinUid=ebb403ee-fe7e-41b1-8f9b-f546acf5ad72&dataSetUid=f2b90e20-0eb0-43da-afc3-0524937ada21
//    https://balticlsc.iem.pw.edu.pl/backend/task/injectData?taskUid=0bbe1d61-f95e-4c04-a373-14704fd3730b&pinUid=b05a416e-56b6-4a80-ba83-cc2e20960af0&dataSetUid=f2b90e20-0eb0-43da-afc3-0524937ada21
//    https://balticlsc.iem.pw.edu.pl/backend/task/injectData?taskUid=0bbe1d61-f95e-4c04-a373-14704fd3730b&pinUid=e4d43e4e-e1a2-4604-bbc7-7ab439b6ae5f&dataSetUid=693e028d-08ab-4b9f-b078-00038a9433af
    suspend fun injectDataToComputationTask(
        taskUid: String,
        pinUid: String,
        dataSetUid: String
    ): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/task/abort") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("taskUid", taskUid)
            parameter("pinUid", pinUid)
            parameter("dataSetUid", dataSetUid)
        }

    }
}