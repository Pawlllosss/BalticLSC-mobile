package pl.oczadly.baltic.lsc.app

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import pl.oczadly.baltic.lsc.app.model.App
import pl.oczadly.baltic.lsc.model.Response


class AppApi {

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

    suspend fun fetchApplications(): Response<App> {
        return client.get("https://dev.balticlsc.iem.pw.edu.pl/app/shelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImRlbW8iLCJzdWIiOiJkZW1vIiwianRpIjoiZGMzNWJiYTkwYWNiNGI3M2FhNzVmYTlmYTIwYTIxY2EiLCJzaWQiOiI2MGQxNDg1MGM5NTQ0NTdlOTFhNTVlYTk0MzAwNjY4MyIsImV4cCI6MTYzNDE0NTk5OSwiaXNzIjoid3V0LmJhbHRpY2xzYy5ldSIsImF1ZCI6Ind1dC5iYWx0aWNsc2MuZXUifQ.ojDooBiuKWHDUbehVH75laUs0ZtcQIo0gUd17MV8Iak")
            }
        }
    }
}