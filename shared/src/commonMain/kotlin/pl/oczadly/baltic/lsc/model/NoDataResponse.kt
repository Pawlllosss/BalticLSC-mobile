package pl.oczadly.baltic.lsc.model

import kotlinx.serialization.Serializable

@Serializable
data class NoDataResponse(
    val success: Boolean,
    val message: String
)
