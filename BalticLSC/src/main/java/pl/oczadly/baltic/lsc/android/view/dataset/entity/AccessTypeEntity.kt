package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class AccessTypeEntity(
    val uid: String,
    val name: String,
    val version: String,
    val accessSchema: Map<String, String>
): Serializable {
    override fun toString(): String {
        return "$name ($version)"
    }
}
