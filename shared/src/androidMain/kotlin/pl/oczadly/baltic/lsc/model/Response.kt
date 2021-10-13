package pl.oczadly.baltic.lsc.model

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val success: Boolean,
    val message: String,
    val data: List<T>
)
