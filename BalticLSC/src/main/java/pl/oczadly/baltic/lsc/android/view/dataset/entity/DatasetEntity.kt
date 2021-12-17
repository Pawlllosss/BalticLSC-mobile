package pl.oczadly.baltic.lsc.android.view.dataset.entity

import pl.oczadly.baltic.lsc.dataset.dto.DatasetMultiplicity
import java.io.Serializable

data class DatasetEntity(
    val name: String,
    val uid: String,
    val datasetMultiplicity: DatasetMultiplicity,
    val dataType: DataTypeEntity,
    val dataStructure: DataStructureEntity?,
    val accessTypeEntity: AccessTypeEntity,
    val accessValues: String,
    val pathValues: String
): Serializable {
    override fun toString(): String {
        return name
    }
}
