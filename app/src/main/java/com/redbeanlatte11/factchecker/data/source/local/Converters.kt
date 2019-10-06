package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redbeanlatte11.factchecker.data.Thumbnail

class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {
        }.type

        return gson.fromJson<List<String>>(value, listType)
    }
}

class ThumbnailsConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromStringMap(map: Map<String, Thumbnail>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun stringToMap(value: String): Map<String, Thumbnail> {
        val mapType = object : TypeToken<Map<String, Thumbnail>>() {}.type
        return gson.fromJson(value, mapType)
    }
}

class LocalizedConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromStringMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun stringToMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }
}