package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetShelfEntity
import pl.oczadly.baltic.lsc.dataset.dto.DatasetShelfItem

class DatasetShelfEntityConverter {

    fun convertFromDatasetShelfItemDTO(datasetShelfItem: DatasetShelfItem): DatasetShelfEntity {
        return DatasetShelfEntity(
            datasetShelfItem.uid,
            datasetShelfItem.name,
            datasetShelfItem.dataTypeUid,
            datasetShelfItem.dataTypeName
        )
    }
}