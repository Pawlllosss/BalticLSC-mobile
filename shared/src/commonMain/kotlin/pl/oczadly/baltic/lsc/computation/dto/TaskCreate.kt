package pl.oczadly.baltic.lsc.computation.dto

import kotlinx.serialization.Serializable

//        {"taskName":"Another test","versionId":"MarekImageProcessor2_rel_001","priority":"3","reservedCredits":"100","isPrivate":false,"invariants":[],"clusterAllocation":"strong","clusterUid":"","auxStorageCredits":"100","minCPUs":"22","maxCPUs":"33","minGPUs":"31","maxGPUs":"45","minMemory":"54","maxMemory":"77","minStorage":"41","maxStorage":"56","fhPolicy":"Break"}
@Serializable
data class TaskCreate(
    val taskName: String,
    val versionId: String,
    val priority: Int,
    val reservedCredits: Int,
    val isPrivate: Boolean,
    val invariants: List<String>,
    val clusterAllocation: String,
    val clusterUid: String,
    val auxStorageCredits: Int,
    val minCPUs: Int,
    val maxCPUs: Int,
    val minGPUs: Int,
    val maxGPUs: Int,
    val minMemory: Int,
    val maxMemory: Int,
    val minStorage: Int,
    val maxStorage: Int,
    val fhPolicy: String
)