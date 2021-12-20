package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.view.dataset.entity.DataStructureEntity
import pl.oczadly.baltic.lsc.dataset.dto.DataStructure

class DataStructureEntityConverter {

    fun convertFromDataStructureDTO(dataStructure: DataStructure): DataStructureEntity {
        return DataStructureEntity(
            dataStructure.isBuiltIn,
            dataStructure.dataSchema,
            dataStructure.name,
            dataStructure.uid,
            dataStructure.version
        )
    }
}
