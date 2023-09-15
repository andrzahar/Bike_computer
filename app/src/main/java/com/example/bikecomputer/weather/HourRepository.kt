package com.example.bikecomputer.weather

import com.example.bikecomputer.common.Resource
import com.example.bikecomputer.weather.models.Hour
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HourRepository (
    private val weatherApi: WeatherApi,
    private val hourDao: HourDao
) {

    fun update(): Flow<Resource<Array<Hour>>> = flow {
        emit(Resource.Loading())
        try {
            val data = weatherApi.get()
            var hours = arrayOf<Hour>()
            data.forecast.forecastday.forEach {
                hours += it.hour
            }
            hourDao.deleteAll()
            hourDao.insertAll(*hours)
            emit(Resource.Success(hours))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error"))
        }
    }

    suspend fun get(): Resource<Array<Hour>> {
        return try {
            Resource.Success(hourDao.getAll())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }
}