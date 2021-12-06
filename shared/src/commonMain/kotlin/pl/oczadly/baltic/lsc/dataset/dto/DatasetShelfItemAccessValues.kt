package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class DatasetShelfItemAccessValues(
    val Host: String,
    val User: String
)
