package pl.oczadly.baltic.lsc.login.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(val token: String)
