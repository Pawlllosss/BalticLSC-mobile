package pl.oczadly.baltic.lsc.util

import com.auth0.jwt.JWT

actual class JWTVerifier {

    actual fun isTokenValid(token: String): Boolean {
        val currentTime = System.currentTimeMillis();
        if ( JWT.decode(token).expiresAt.time < currentTime) {
            return false
        }
        return true
    }
}