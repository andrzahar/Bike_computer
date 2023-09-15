package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Forecastday (

  @SerialName("date"       ) var date      : String,
  @SerialName("date_epoch" ) var dateEpoch : Long,
  @SerialName("day"        ) var day       : Day,
  @SerialName("astro"      ) var astro     : Astro,
  @SerialName("hour"       ) var hour      : ArrayList<Hour>

)