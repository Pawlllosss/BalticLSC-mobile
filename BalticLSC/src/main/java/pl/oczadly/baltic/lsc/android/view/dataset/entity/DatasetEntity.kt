package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class DatasetEntity(
    val name: String,
    val uid: String,
    val multiplicityByValue: Pair<Int, String>,
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
