package com.example.bikecomputer.weather.models

import kotlinx.serialization.*

@Serializable
data class WeatherModel (
  var location: Location,
  var current: Current,
  var forecast: Forecast
)