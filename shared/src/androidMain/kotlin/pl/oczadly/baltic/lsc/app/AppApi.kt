package pl.oczadly.baltic.lsc.app

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import pl.oczadly.baltic.lsc.app.model.App
import pl.oczadly.baltic.lsc.model.Response


class AppApi() {
//    @Suppress("CanBePrimaryConstructorProperty")
//    private val log = log

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
//        install(Logging) {
//            logger = object : Logger {
//                override fun log(message: String) {
//                    log.v("Network") { message }
//                }
//            }
//
//            level = LogLevel.INFO
//        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    init {
//        ensureNeverFrozen()
    }

    suspend fun fetchApplications(): Response<App> {
//        log.d { "Fetching Breeds from network" }
        return client.get<Response<App>>("https://dev.balticlsc.iem.pw.edu.pl/app/shelf/") {
            headers {
                append("Accept", "application/json")
                append("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImRlbW8iLCJzdWIiOiJkZW1vIiwianRpIjoiMGY5NmVjZWI4NzY5NDkxNjhmZGI3YjJlNzBkMTEwMGQiLCJzaWQiOiI1MWQ3NjEzYTc1ZTE0YjFiOTcwMjYwMDliNjM4NmI5OSIsImV4cCI6MTYzMzQ2MTA0OSwiaXNzIjoid3V0LmJhbHRpY2xzYy5ldSIsImF1ZCI6Ind1dC5iYWx0aWNsc2MuZXUifQ.GJU8B4Pz3FuHaBDdZ5QPQtqFJe8so3-tbgiguABYj5E")
            }
        }
    }
}