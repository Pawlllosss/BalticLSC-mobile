package pl.oczadly.baltic.lsc.android.view.app.entity

import java.io.Serializable

data class AppResourceRangeEntity(
    val minCPUs: Int,
    val maxCPUs: Int,
    val minGPUs: Int,
    val maxGPUs: Int,
    val minMemory: Int,
    val maxMemory: Int,
    val minStorage: Int,
    val maxStorage: Int
): Serializable
