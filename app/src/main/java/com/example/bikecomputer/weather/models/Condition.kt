package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Condition (

  @SerialName("text" ) var text : String,
  @SerialName("icon" ) var icon : String,
  @SerialName("code" ) var code : Int

)