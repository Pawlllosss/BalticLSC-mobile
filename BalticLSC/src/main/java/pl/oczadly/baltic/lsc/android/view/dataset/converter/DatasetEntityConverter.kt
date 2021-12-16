package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetShelfEntity

class DatasetEntityConverter {

    fun convertToDatasetEntity(datasetShelfEntity: DatasetShelfEntity): DatasetEntity {
        return DatasetEntity(
            datasetShelfItem.uid,
            datasetShelfItem.name,
            datasetShelfItem.dataTypeUid,
            datasetShelfItem.dataTypeName
        )
    }
}