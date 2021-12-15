package pl.oczadly.baltic.lsc.android.view.dataset.entity

import pl.oczadly.baltic.lsc.dataset.dto.DatasetMultiplicity

data class DatasetEntity(
    val name: String,
    val multiplicity: DatasetMultiplicity,
    val dataType: DataTypeEntity,
    val dataStructure: DataStructureEntity?,
    val accessTypeEntity: AccessTypeEntity,
    val accessValues: String,
    val pathValues: String
)
