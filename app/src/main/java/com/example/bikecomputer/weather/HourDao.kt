package com.example.bikecomputer.weather

import androidx.room.*
import com.example.bikecomputer.weather.models.Hour

@Dao
interface HourDao {

    @Insert
    suspend fun insertAll(vararg hours: Hour)

    @Query("SELECT * FROM hour")
    suspend fun getAll(): Array<Hour>

    @Query("DELETE FROM hour")
    suspend fun deleteAll()
}