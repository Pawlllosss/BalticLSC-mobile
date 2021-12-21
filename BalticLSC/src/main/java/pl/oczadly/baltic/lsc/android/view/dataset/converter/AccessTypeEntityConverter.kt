package pl.oczadly.baltic.lsc.android.view.dataset.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.oczadly.baltic.lsc.android.view.dataset.entity.AccessTypeEntity
import pl.oczadly.baltic.lsc.dataset.dto.AccessType

class AccessTypeEntityConverter(private val gson: Gson) {

    fun convertFromAccessTypeDTO(accessType: AccessType): AccessTypeEntity {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        val accessSchema = accessType.accessSchema

        return AccessTypeEntity(
            accessType.uid,
            accessType.name,
            accessType.version,
            if (accessSchema.isNullOrBlank()) emptyMap() else gson.fromJson(accessSchema, mapType),
            gson.fromJson(accessType.pathSchema, mapType)
        )
    }
}
