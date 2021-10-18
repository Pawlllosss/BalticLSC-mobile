package pl.oczadly.baltic.lsc.app

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import pl.oczadly.baltic.lsc.app.model.AppShelf
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

    suspend fun fetchApplicationShelf(): Response<AppShelf> {
        return client.get("https://dev.balticlsc.iem.pw.edu.pl/app/shelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImRlbW8iLCJzdWIiOiJkZW1vIiwianRpIjoiYTBiMzJjYzllNTNmNGUwZWE0MTVlZDM3OGUwYzhkZTAiLCJzaWQiOiIyODJkMTJiNzgwYTI0OTE2OTdjMDQ4MmQ1MWM2ZWQwMyIsImV4cCI6MTYzNDU3NjkyOSwiaXNzIjoid3V0LmJhbHRpY2xzYy5ldSIsImF1ZCI6Ind1dC5iYWx0aWNsc2MuZXUifQ.iB0b5XRHkbJqR5eRN87srd45ikPnkYV3afQKB1yS2Fw")
            }
        }
    }
}