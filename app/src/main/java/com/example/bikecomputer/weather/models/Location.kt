package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Location (

  @SerialName("name"            ) var name           : String,
  @SerialName("region"          ) var region         : String,
  @SerialName("country"         ) var country        : String,
  @SerialName("lat"             ) var lat            : Double,
  @SerialName("lon"             ) var lon            : Double,
  @SerialName("tz_id"           ) var tzId           : String,
  @SerialName("localtime_epoch" ) var localtimeEpoch : Long,
  @SerialName("localtime"       ) var localtime      : String

)