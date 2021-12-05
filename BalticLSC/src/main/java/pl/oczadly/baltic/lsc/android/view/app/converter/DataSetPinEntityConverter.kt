package pl.oczadly.baltic.lsc.android.view.app.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.DatasetPinEntity
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetPin

class DataSetPinEntityConverter {

    fun convertFromDataSetPinDTO(datasetPin: DatasetPin): DatasetPinEntity = DatasetPinEntity(
        datasetPin.uid,
        datasetPin.name,
        datasetPin.binding,
        datasetPin.dataTypeUid,
        datasetPin.dataTypeName,
        datasetPin.accessTypeUid,
        datasetPin.accessTypeName
    )
}