package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataStructure(
    val isBuiltIn: Boolean,
    val dataSchema: String,
    val name: String,
    val uid: String,
    val version: String
)
