package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Astro (

  @SerialName("sunrise"           ) var sunrise          : String,
  @SerialName("sunset"            ) var sunset           : String,
  @SerialName("moonrise"          ) var moonrise         : String,
  @SerialName("moonset"           ) var moonset          : String,
  @SerialName("moon_phase"        ) var moonPhase        : String,
  @SerialName("moon_illumination" ) var moonIllumination : String,
  @SerialName("is_moon_up"        ) var isMoonUp         : Int,
  @SerialName("is_sun_up"         ) var isSunUp          : Int

)