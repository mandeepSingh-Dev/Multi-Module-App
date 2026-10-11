package com.app.core_common.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> String.fromJson() : T? {
    return try {
        val type = object : TypeToken<T>() {}.type
        Log.d("ckcnskcns", type.toString())
        Gson().fromJson(this, type)
    }catch (e:Exception){
        Log.e("GSON_PARSE_ERROR", e.toString())
        null
    }
}

inline fun <reified T> String.fromJsonArray() : List<T>? {
    return try {
        val type = object : TypeToken<List<T>>() {}.type
        Gson().fromJson(this, type)
    }catch (e:Exception){
        Log.e("GSON_PARSE_ERROR", e.toString())
        null
    }
}

fun <T> T.toJson() : String? {
    return try {
        Gson().toJson(this)
    }catch (e: Exception){
        Log.e("GSON_PARSE_ERROR", e.toString())
        null
    }
}
