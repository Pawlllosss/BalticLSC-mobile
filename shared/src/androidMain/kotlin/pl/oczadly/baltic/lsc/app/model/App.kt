package pl.oczadly.baltic.lsc.app.model

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val name: String,
    val icon: String
)
