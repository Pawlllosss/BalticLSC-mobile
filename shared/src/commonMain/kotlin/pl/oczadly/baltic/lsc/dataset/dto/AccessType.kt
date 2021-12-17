package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccessType(
    val isBuiltIn: Int,
    val accessSchema: String,
    val pathSchema: String,
    val name: String,
    val uid: String,
    val version: String
)