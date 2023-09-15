package com.example.bikecomputer.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Day (

  @SerialName("maxtemp_c"            ) var maxtempC          : Double,
  @SerialName("maxtemp_f"            ) var maxtempF          : Double,
  @SerialName("mintemp_c"            ) var mintempC          : Double,
  @SerialName("mintemp_f"            ) var mintempF          : Double,
  @SerialName("avgtemp_c"            ) var avgtempC          : Double,
  @SerialName("avgtemp_f"            ) var avgtempF          : Double,
  @SerialName("maxwind_mph"          ) var maxwindMph        : Double,
  @SerialName("maxwind_kph"          ) var maxwindKph        : Double,
  @SerialName("totalprecip_mm"       ) var totalprecipMm     : Double,
  @SerialName("totalprecip_in"       ) var totalprecipIn     : Double,
  @SerialName("totalsnow_cm"         ) var totalsnowCm       : Double,
  @SerialName("avgvis_km"            ) var avgvisKm          : Double,
  @SerialName("avgvis_miles"         ) var avgvisMiles       : Double,
  @SerialName("avghumidity"          ) var avghumidity       : Double,
  @SerialName("daily_will_it_rain"   ) var dailyWillItRain   : Int,
  @SerialName("daily_chance_of_rain" ) var dailyChanceOfRain : Int,
  @SerialName("daily_will_it_snow"   ) var dailyWillItSnow   : Int,
  @SerialName("daily_chance_of_snow" ) var dailyChanceOfSnow : Int,
  @SerialName("condition"            ) var condition         : Condition,
  @SerialName("uv"                   ) var uv                : Double

)