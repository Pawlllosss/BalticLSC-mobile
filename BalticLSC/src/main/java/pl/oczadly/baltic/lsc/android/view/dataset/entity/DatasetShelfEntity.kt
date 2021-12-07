package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class DatasetShelfEntity(
    val uid: String,
    val name: String,
    val dataTypeUid: String,
    val dataTypeName: String,
): Serializable {
    override fun toString(): String {
        return name
    }
}
