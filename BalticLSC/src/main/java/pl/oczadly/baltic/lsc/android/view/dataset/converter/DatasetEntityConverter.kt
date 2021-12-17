package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem

class DatasetEntityConverter {

    fun convertFromDatasetShelfItemDTO(dataset: DatasetShelfItem): DatasetEntity {
        return DatasetEntity(
            dataset.name,
            dataset.uid,
            dataset.multiplicity,
            DataTypeEntity(dataset.dataTypeUid, dataset.dataTypeName, dataset.dataTypeVersion),
            createDataStructureEntityIfPresent(dataset),
            AccessTypeEntity(dataset.accessTypeUid, dataset.accessTypeName, dataset.accessTypeVersion),
            dataset.accessValues,
            dataset.values
        )
    }

    private fun createDataStructureEntityIfPresent(dataset: DatasetShelfItem): DataStructureEntity? {
        if (dataset.dataStructureName != null && dataset.dataStructureUid != null && dataset.dataStructureVersion != null) {
            return DataStructureEntity(dataset.dataStructureUid!!, dataset.dataStructureName!!, dataset.dataStructureVersion!!)
        }
        return null
    }
}