package pl.oczadly.baltic.lsc.android.util

import org.json.JSONArray
import org.json.JSONObject

fun convertFromJsonToMap(json: String): Map<String, *> {
    val jsonObject = JSONObject(json)
    return jsonObject.toMap()
}

private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is JSONArray ->
        {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}