package pl.oczadly.baltic.lsc

import kotlinx.serialization.Serializable

@Serializable
data class QueryParams(
    val appUid: String
)
