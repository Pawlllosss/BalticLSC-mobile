package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class DataTypeEntity(
    val isStructured: Boolean,
    val uid: String,
    val name: String,
    val version: String
): Serializable {
    override fun toString(): String {
        return "$name ($version)"
    }
}
