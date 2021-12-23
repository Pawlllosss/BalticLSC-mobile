package pl.oczadly.baltic.lsc.dataset

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
import pl.oczadly.baltic.lsc.UserState
import pl.oczadly.baltic.lsc.dataset.dto.AccessType
import pl.oczadly.baltic.lsc.dataset.dto.DataStructure
import pl.oczadly.baltic.lsc.dataset.dto.DataType
import pl.oczadly.baltic.lsc.dataset.dto.DatasetCreate
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem
import pl.oczadly.baltic.lsc.model.NoDataResponse
import pl.oczadly.baltic.lsc.model.Response
import pl.oczadly.baltic.lsc.model.SingleResponse


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

    suspend fun fetchDataTypes(): Response<DataType> {
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/task/dataTypes/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun fetchDataStructures(): Response<DataStructure> {
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/task/dataStructures/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun fetchDatasetShelf(): Response<DatasetShelfItem> {
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/task/dataShelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun fetchAccessTypes(): Response<AccessType> {
        return client.get("https://balticlsc.iem.pw.edu.pl/backend/task/accessTypes/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
        }
    }

    suspend fun addDataset(dataset: DatasetCreate): SingleResponse<String> {
        return client.put("https://balticlsc.iem.pw.edu.pl/backend/task/dataSet/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = dataset
        }
    }

    // yup, the post method is not a mistake
    suspend fun updateDataset(dataset: DatasetCreate): NoDataResponse {
        return client.post("https://balticlsc.iem.pw.edu.pl/backend/task/dataSet/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
                append("Content-Type", "application/json")
            }
            body = dataset
        }
    }

    suspend fun archiveDataset(datasetUid: String): NoDataResponse {
        return client.delete("https://balticlsc.iem.pw.edu.pl/backend/task/dataSet/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer ${userState.accessToken}")
            }
            parameter("dataSetUid", datasetUid)
        }
    }
}
