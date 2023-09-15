package com.example.bikecomputer.weather

import com.example.bikecomputer.weather.models.Hour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

private suspend fun <T> actionInCoroutine(action: suspend () -> T) = coroutineScope {
    async(Dispatchers.IO) {
        return@async action()
    }
}.await()

class PersonsDataSource @Inject constructor(private val hourDao: HourDao) : HourDao {

    override suspend fun insertAll(vararg hours: Hour) = actionInCoroutine {
        hourDao.insertAll(*hours)
    }

    override suspend fun getAll() = actionInCoroutine {
        hourDao.getAll()
    }

    override suspend fun deleteAll() = actionInCoroutine {
        hourDao.deleteAll()
    }
}