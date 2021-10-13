package pl.oczadly.baltic.lsc.model

data class Response<T>(
    val success: Boolean,
    val message: String,
    val data: List<T>
)
