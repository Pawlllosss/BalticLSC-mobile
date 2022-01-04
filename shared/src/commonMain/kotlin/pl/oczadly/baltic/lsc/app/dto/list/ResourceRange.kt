package pl.oczadly.baltic.lsc.app.dto.list

import kotlinx.serialization.Serializable

@Serializable
data class ResourceRange(
    val minCPUs: Int,
    val maxCPUs: Int,
    val minGPUs: Int,
    val maxGPUs: Int,
    val minMemory: Int,
    val maxMemory: Int,
    val minStorage: Int,
    val maxStorage: Int
)
