package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataType(
    val isBuiltIn: Boolean,
    val isStructured: Boolean,
    val name: String,
    val uid: String,
    val version: String
)
