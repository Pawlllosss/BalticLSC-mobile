package pl.oczadly.baltic.lsc.dataset.dto

import kotlinx.serialization.Serializable

@Serializable
data class DatasetShelfItem(
    val uid: String,
    val name: String,
    val dataTypeUid: String,
    val dataTypeName: String,
    val dataTypeVersion: String,
    val multiplicity: Int,
    val dataStructureUid: String?,
    val dataStructureName: String?,
    val dataStructureVersion: String?,
    val accessTypeUid: String,
    val accessTypeName: String,
    val accessTypeVersion: String,
    val values: String // no comment, values: "{\n  \"ResourcePath\" : \"/files/edger/source1\"\n}"
)