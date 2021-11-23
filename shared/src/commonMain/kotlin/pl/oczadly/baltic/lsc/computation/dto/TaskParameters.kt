package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskParameters(
    val taskName: String,
    val priority: Int
)
