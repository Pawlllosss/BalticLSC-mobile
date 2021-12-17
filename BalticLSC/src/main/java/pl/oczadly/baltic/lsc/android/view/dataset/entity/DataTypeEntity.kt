package pl.oczadly.baltic.lsc.android.view.dataset.entity

import java.io.Serializable

data class DataTypeEntity(
    val uid: String,
    val name: String,
    val version: String
): Serializable
