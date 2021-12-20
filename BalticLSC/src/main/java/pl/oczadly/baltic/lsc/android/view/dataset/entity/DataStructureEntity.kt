package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class DataStructureEntity(
    val isBuiltIn: Boolean,
    val dataSchema: String,
    val name: String,
    val uid: String,
    val version: String
): Serializable {
    override fun toString(): String {
        return "$name ($version)"
    }
}
