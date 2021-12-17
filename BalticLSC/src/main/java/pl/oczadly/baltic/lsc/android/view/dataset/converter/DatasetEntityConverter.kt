package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem

class DatasetEntityConverter {

    fun convertToDatasetEntity(datasetShelfEntity: DatasetShelfEntity): DatasetEntity {
        return DatasetEntity(
            datasetShelfItem.uid,
            datasetShelfItem.name,
            datasetShelfItem.dataTypeUid,
            datasetShelfItem.dataTypeName
        )
    }

    private fun createDataStructureEntityIfPresent(dataset: DatasetShelfItem): DataStructureEntity? {
        if (dataset.dataStructureName != null && dataset.dataStructureUid != null && dataset.dataStructureVersion != null) {
            return DataStructureEntity(dataset.dataStructureUid!!, dataset.dataStructureName!!, dataset.dataStructureVersion!!)
        }
        return null
    }
}