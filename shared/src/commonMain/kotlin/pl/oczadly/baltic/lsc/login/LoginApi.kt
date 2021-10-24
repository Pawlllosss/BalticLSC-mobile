package pl.oczadly.baltic.lsc.login

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import pl.oczadly.baltic.lsc.login.model.Login
import pl.oczadly.baltic.lsc.login.model.LoginRequest
import pl.oczadly.baltic.lsc.model.SingleResponse


class LoginApi {

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

    suspend fun login(
        loginRequest: LoginRequest = LoginRequest(
            "demo",
            "BalticDemo"
        )
    ): SingleResponse<Login> {
        return client.post("https://dev.balticlsc.iem.pw.edu.pl/Login") {
            headers {
                append("Accept", "application/json")
                append("Content-Type", "application/json")
            }
            body = loginRequest
        }
    }
}