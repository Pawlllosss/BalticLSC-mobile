package pl.oczadly.baltic.lsc.model

import kotlinx.serialization.Serializable

@Serializable
data class SingleResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
