package pl.oczadly.baltic.lsc.android.view.dataset.converter

import com.google.gson.Gson
import pl.oczadly.baltic.lsc.android.util.convertFromJsonStringToMap
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.dataset.dto.AccessType

class AccessTypeEntityConverter(private val gson: Gson) {

    fun convertFromAccessTypeDTO(accessType: AccessType): AccessTypeEntity {
        val accessSchema = accessType.accessSchema

        return AccessTypeEntity(
            accessType.uid,
            accessType.name,
            accessType.version,
            if (accessSchema.isNullOrBlank()) emptyMap() else convertFromJsonStringToMap(
                gson,
                accessSchema
            ),
            convertFromJsonStringToMap(gson, accessType.pathSchema)
        )
    }
}
