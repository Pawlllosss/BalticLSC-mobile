package pl.oczadly.baltic.lsc.android.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun convertFromJsonStringToMap(gson:Gson, jsonString: String): Map<String, String> {
    val mapType = object : TypeToken<Map<String, String>>() {}.type
    return gson.fromJson(jsonString, mapType)
}
