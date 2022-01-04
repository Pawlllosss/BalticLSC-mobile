package pl.oczadly.baltic.lsc.app.dto

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.app.dto.list.ResourceRange

//{"uid":"ce943f0f-126b-4963-945c-f4282d4a6e51","openSource":true,"version":"Another test","description":null,"supportedResourcesRange":{"minCPUs":0,"minGPUs":0,"minMemory":0,"minStorage":0,"maxCPUs":0,"maxGPUs":0,"maxMemory":0,"maxStorage":0}}
//{"uid":"6239c353-b074-4fd0-8781-25d4d8b4fd8c","openSource":false,"version":"test release 2","description":"Modified description","supportedResourcesRange":{"minCPUs":0,"minGPUs":0,"minMemory":0,"minStorage":0,"maxCPUs":0,"maxGPUs":0,"maxMemory":0,"maxStorage":0}}
@Serializable
data class AppReleaseEdit(
    val uid: String,
    val openSource: Boolean,
    val version: String,
    val description: String,
    val supportedResourcesRange: ResourceRange
)
