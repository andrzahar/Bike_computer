package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Forecast (

  @SerialName("forecastday" ) var forecastday : ArrayList<Forecastday>

)