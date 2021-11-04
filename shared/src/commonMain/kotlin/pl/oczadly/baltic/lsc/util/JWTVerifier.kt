package pl.oczadly.baltic.lsc.util

expect class JWTVerifier {
    fun isTokenValid(token: String): Boolean
}