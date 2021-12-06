package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class DatasetShelfItemValues(
    val ResourcePath: String,
    val accessValues: DatasetShelfItemAccessValues
)
