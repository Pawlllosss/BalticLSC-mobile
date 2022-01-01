package pl.oczadly.baltic.lsc.app.dto

import kotlinx.serialization.Serializable

//{"name":"test","uid":"f30717af-6fa6-4a54-a55f-6870ab7a14d1","pClass":"fdfd","shortDescription":null,"longDescription":null,"keywords":["terst","sadsd"],"icon":"https://www.balticlsc.eu/model/_icons/default.png","isApp":true}
@Serializable
data class AppEdit(
    val name: String,
    val uid: String,
    val pClass: String,
    val shortDescription: String,
    val longDescription: String,
    val keywords: List<String>,
    val icon: String,
    val isApp: Boolean
)
