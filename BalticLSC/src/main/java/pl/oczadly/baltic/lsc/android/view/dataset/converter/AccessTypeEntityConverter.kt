package pl.oczadly.baltic.lsc.android.view.dataset.converter

import pl.oczadly.baltic.lsc.android.util.convertFromJsonToMap
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.dataset.dto.AccessType

class AccessTypeEntityConverter {

    fun convertFromAccessTypeDTO(accessType: AccessType): AccessTypeEntity {
        return AccessTypeEntity(
            accessType.uid,
            accessType.name,
            accessType.version,
            convertJsonToAccessValues(accessType.accessSchema)
        )
    }

    private fun convertJsonToAccessValues(accessSchema: String): Map<String, String> =
        if (accessSchema.isNullOrBlank()) emptyMap() else
            convertFromJsonToMap(accessSchema)
                .mapValues { it.toString() }
}
