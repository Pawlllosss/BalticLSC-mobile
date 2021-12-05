package pl.oczadly.baltic.lsc.app.dto.dataset

import kotlinx.serialization.Serializable

@Serializable
data class DatasetPin(
    val uid: String,
    val name: String,
    val binding: DatasetBinding,
    val dataTypeUid: String,
    val dataTypeName: String,
    val accessTypeUid: String,
    val accessTypeName: String
)
