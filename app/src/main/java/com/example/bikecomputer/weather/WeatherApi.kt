package com.example.bikecomputer.weather

import com.example.bikecomputer.weather.models.WeatherModel
import io.ktor.client.*
import io.ktor.client.request.*

class WeatherApi(private val client: HttpClient) {

    suspend fun get(): WeatherModel {
        return client.get(
            urlString = "https://api.weatherapi.com/v1/forecast.json?key=03ddebe545f84607b06122735231905&q=Orenburg&days=3&aqi=no&alerts=no"
        )
    }
}