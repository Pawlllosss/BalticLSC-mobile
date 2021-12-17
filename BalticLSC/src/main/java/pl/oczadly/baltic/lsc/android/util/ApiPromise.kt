package pl.oczadly.baltic.lsc.android.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.lazyPromise

fun <T> createApiPromise(requestFunction: suspend () -> List<T>) = lazyPromise {
    withContext(Dispatchers.IO) {
        try {
            return@withContext requestFunction()
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext listOf<T>()
        }
    }
}
