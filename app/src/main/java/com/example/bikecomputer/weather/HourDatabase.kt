package com.example.bikecomputer.weather

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bikecomputer.weather.models.ConditionConverter
import com.example.bikecomputer.weather.models.Hour

@Database(entities = [Hour::class], version = 1)
@TypeConverters(ConditionConverter::class)
abstract class HourDatabase : RoomDatabase() {
    abstract fun personDao(): HourDao
}