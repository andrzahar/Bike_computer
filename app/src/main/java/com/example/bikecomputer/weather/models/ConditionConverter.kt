package com.example.bikecomputer.weather.models

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConditionConverter {

    @TypeConverter
    fun String.toCondition(): Condition = Json.decodeFromString(this)

    @TypeConverter
    fun Condition.toString(): String = Json.encodeToString(this)
}