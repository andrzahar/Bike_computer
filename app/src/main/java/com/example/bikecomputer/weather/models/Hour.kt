package com.example.bikecomputer.weather.models

import androidx.room.Entity
import androidx.room.TypeConverters
import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
@Entity(
  primaryKeys = ["timeEpoch"]
)
data class Hour (

  @SerialName("time_epoch"     ) var timeEpoch    : Long,
  @SerialName("time"           ) var time         : String,
  @SerialName("temp_c"         ) var tempC        : Double,
  @SerialName("temp_f"         ) var tempF        : Double,
  @SerialName("is_day"         ) var isDay        : Int,
  @SerialName("condition"      ) var condition    : Condition,
  @SerialName("wind_mph"       ) var windMph      : Double,
  @SerialName("wind_kph"       ) var windKph      : Double,
  @SerialName("wind_degree"    ) var windDegree   : Int,
  @SerialName("wind_dir"       ) var windDir      : String,
  @SerialName("pressure_mb"    ) var pressureMb   : Double,
  @SerialName("pressure_in"    ) var pressureIn   : Double,
  @SerialName("precip_mm"      ) var precipMm     : Double,
  @SerialName("precip_in"      ) var precipIn     : Double,
  @SerialName("humidity"       ) var humidity     : Int,
  @SerialName("cloud"          ) var cloud        : Int,
  @SerialName("feelslike_c"    ) var feelslikeC   : Double,
  @SerialName("feelslike_f"    ) var feelslikeF   : Double,
  @SerialName("windchill_c"    ) var windchillC   : Double,
  @SerialName("windchill_f"    ) var windchillF   : Double,
  @SerialName("heatindex_c"    ) var heatindexC   : Double,
  @SerialName("heatindex_f"    ) var heatindexF   : Double,
  @SerialName("dewpoint_c"     ) var dewpointC    : Double,
  @SerialName("dewpoint_f"     ) var dewpointF    : Double,
  @SerialName("will_it_rain"   ) var willItRain   : Int,
  @SerialName("chance_of_rain" ) var chanceOfRain : Int,
  @SerialName("will_it_snow"   ) var willItSnow   : Int,
  @SerialName("chance_of_snow" ) var chanceOfSnow : Int,
  @SerialName("vis_km"         ) var visKm        : Double,
  @SerialName("vis_miles"      ) var visMiles     : Double,
  @SerialName("gust_mph"       ) var gustMph      : Double,
  @SerialName("gust_kph"       ) var gustKph      : Double,
  @SerialName("uv"             ) var uv           : Double

)