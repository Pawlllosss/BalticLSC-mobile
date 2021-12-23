package pl.oczadly.baltic.lsc.android.util

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.lazyPromise
import pl.oczadly.baltic.lsc.model.NoDataResponse
import pl.oczadly.baltic.lsc.model.SingleResponse

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

fun <T> createApiPromiseSingleResponse(requestFunction: suspend () -> SingleResponse<T>) = lazyPromise {
    withContext(Dispatchers.IO) {
        try {
            return@withContext requestFunction().data
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}

fun createApiPromiseNoDataResponse(requestFunction: suspend () -> NoDataResponse) = lazyPromise {
    withContext(Dispatchers.IO) {
        try {
            requestFunction()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

suspend fun <T> awaitPromise(lazyPromise: Lazy<Deferred<T>>): T = lazyPromise.value.await()
