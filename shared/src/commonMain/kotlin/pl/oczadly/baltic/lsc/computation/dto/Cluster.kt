package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.serialization.Serializable

@Serializable
data class Cluster(
    val uid: String,
    val name: String
)
