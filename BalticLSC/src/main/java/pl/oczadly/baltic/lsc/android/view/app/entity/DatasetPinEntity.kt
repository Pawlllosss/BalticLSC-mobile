package pl.oczadly.baltic.lsc.android.view.app.entity

import java.io.Serializable
import pl.oczadly.baltic.lsc.app.dto.dataset.DatasetBinding

data class DatasetPinEntity(
    val uid: String,
    val name: String,
    val binding: DatasetBinding,
    val dataTypeUid: String,
    val dataTypeName: String,
    val accessTypeUid: String?,
    val accessTypeName: String?
): Serializable
