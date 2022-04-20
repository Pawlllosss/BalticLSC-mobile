package pl.oczadly.baltic.lsc.diagram.dto

import kotlinx.serialization.Serializable

@Serializable
data class Compartment(
    val id: String,
    val input: String,
    val value: String,
    val elementId: String,
    val compartmentTypeId: String
)
