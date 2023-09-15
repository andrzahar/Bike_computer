package com.example.bikecomputer

data class WorkerState(
    val brake: Boolean,
    val leftShift: Byte,
    val rightShift: Byte,
    var recommendedLeftShift: Byte?,
    var recommendedRightShift: Byte?,
    val speed: Double,
    val gyroscope: Float,
    val compass: Int?,
    val angle: Int?,
    val windSpeed: Double?
)
