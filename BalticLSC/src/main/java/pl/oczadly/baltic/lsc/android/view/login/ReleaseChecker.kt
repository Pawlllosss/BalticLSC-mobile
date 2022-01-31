package pl.oczadly.baltic.lsc.android.view.login

import java.lang.IllegalStateException
import java.time.Instant

class ReleaseChecker {

    fun validateRelease() {
        if (Instant.now().isAfter(Instant.parse("2022-07-10T08:00:00Z"))) {
            throw IllegalStateException("This version of app can't be used anymore")
        }
    }
}