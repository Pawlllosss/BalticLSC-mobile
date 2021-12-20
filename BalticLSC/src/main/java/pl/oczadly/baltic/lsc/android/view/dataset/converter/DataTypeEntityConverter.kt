package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataTypeEntity
import pl.oczadly.baltic.lsc.dataset.dto.DataType

class DataTypeEntityConverter {

    fun convertFromDataTypeDTO(dataType: DataType): DataTypeEntity {
        return DataTypeEntity(
            dataType.isStructured,
            dataType.uid,
            dataType.name,
            dataType.version
        )
    }
}
