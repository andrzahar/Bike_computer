package com.example.bikecomputer.injection

import android.content.Context
import androidx.room.Room
import com.example.bikecomputer.MainWorker
import com.example.bikecomputer.weather.HourDao
import com.example.bikecomputer.weather.HourDatabase
import com.example.bikecomputer.weather.HourRepository
import com.example.bikecomputer.weather.WeatherApi
import com.example.bikecomputer.weather.models.ConditionConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    @Provides
    @Singleton
    fun provideWeatherApi(client: HttpClient): WeatherApi = WeatherApi(client)

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext appContext: Context): HourDatabase =
        Room.databaseBuilder(
            appContext,
            HourDatabase::class.java,
            "hour.db"
        ).build()

    @Provides
    fun provideFavoriteDao(database: HourDatabase): HourDao = database.personDao()

    @Provides
    @Singleton
    fun provideHourRepository(
        weatherApi: WeatherApi,
        hourDao: HourDao
    ): HourRepository = HourRepository(weatherApi, hourDao)

    @Provides
    @Singleton
    fun provideMainWorker(
        @ApplicationContext appContext: Context,
        hourRepository: HourRepository
    ): MainWorker =
        MainWorker(appContext, hourRepository)
}