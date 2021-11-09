package pl.oczadly.baltic.lsc.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val uid: String,
    val name: String,
    val icon: String,
    val shortDescription: String?
)
