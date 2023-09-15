package com.example.bikecomputer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.example.bikecomputer.weather.HourRepository
import com.example.bikecomputer.weather.models.Hour
import io.ktor.util.date.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")
class MainWorker(
    private val appContext: Context,
    private val hourRepository: HourRepository
) : LocationListener, SensorEventListener {

    private val leftShiftValues = 1..3
    private val rightShiftValues = 1..8

    private val locationManager: LocationManager =
        appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val sensorManager: SensorManager =
        appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var brake: Boolean = false
    private var leftShift: Byte = 2
    private var rightShift: Byte = 3

    private var speed: Float = 0.0f
    private var gyroscope: Float = 0f

    private var accelerometer: FloatArray? = null
    private var magneticField: FloatArray? = null

    private var hours: Array<Hour>? = null
    private var windAngleNow: Int? = null
    private var windSpeedNow: Double? = null

    private lateinit var workerState: WorkerState

    private lateinit var fileStream: FileOutputStream

    fun start(): Flow<WorkerState> {
        registerListeners()
        val nameFile = "${getTimeMillis()}.txt"
        fileStream = appContext.openFileOutput(nameFile, MODE_PRIVATE)
        return channelFlow {
            launch {
                withContext(Dispatchers.Default) {
                    mainWork()
                }
            }
        }
    }

    private fun registerListeners() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            500,
            0f,
            this
        )

        val sensorsList = listOf(
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_MAGNETIC_FIELD
        )

        sensorsList.forEach {
            sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(it),
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    private suspend fun ProducerScope<WorkerState>.mainWork() {
        hours = hourRepository.get().data
        updateWindData()
        while (true) {
            updateWorkerState()
            //writeToFile()
            runTensorflow()
            send(workerState)
            delay(500)
        }
    }

    private fun updateWorkerState() {
        val compass = getCompass()
        workerState = WorkerState(
            brake,
            leftShift,
            rightShift,
            null,
            null,
            getSpeed(),
            gyroscope,
            compass,
            getAngle(compass),
            windSpeedNow
        )
    }

    private fun runTensorflow() {
        if (workerState.angle == null || workerState.windSpeed == null) return
        try {
            val interpreter = Interpreter(FileUtil.loadMappedFile(appContext, "model.tflite"))
            val input = floatArrayOf(
                if (workerState.brake) 1.0f else 0.0f,
                workerState.speed.toFloat(),
                workerState.gyroscope,
                workerState.angle!!.toFloat(),
                workerState.windSpeed!!.toFloat()
            )
            val output = Array(1) {
                FloatArray(9) { .0f }
            }
            interpreter.run(input, output)
            val max = output[0].max()
            val shift = output[0].indexOfFirst { it == max } + 2
            val res = when (shift) {
                in 0..3 -> Pair(1, shift)
                in 4..7 -> Pair(2, shift - 1)
                else -> Pair(3, shift - 2)
            }
            workerState.recommendedLeftShift = res.first.toByte()
            workerState.recommendedRightShift = res.second.toByte()
        } catch (e: Exception) {
            Log.d("ttt", e.message?: "Error")
        }
    }

    private fun getAngle(compass: Int?): Int? {
        if (compass == null || windAngleNow == null) return null
        var res = abs(compass - windAngleNow!!)
        if (res > 180) {
            res = 360 - res
        }
        return res
    }

    private fun updateWindData() {
        hours?.let {
            val timeNow = getTimeMillis() / 1000
            for (i in it.indices) {
                if (it[i + 1].timeEpoch > timeNow) {
                    windAngleNow = it[i].windDegree
                    windSpeedNow = it[i].windKph
                    break
                }
            }
        }
    }

    /*private fun writeToFile() {
        var text = ""
        workerState.apply {
            text += if (brake) 1 else 0
            text += " $speed"
            text += " $gyroscope"
            text += " $compass"
            text += " $angle"
            text += " $windSpeed"
            text += " $shift\n"
        }
        fileStream.write(text.toByteArray())
    }*/

    override fun onLocationChanged(location: Location) {
        speed = location.speed
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        when (event.sensor.type) {
            Sensor.TYPE_GYROSCOPE -> {
                gyroscope = event.values[0]
            }
            Sensor.TYPE_ACCELEROMETER -> {
                accelerometer = event.values
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magneticField = event.values
            }
        }
    }

    private fun getSpeed(): Double = (speed * 3.6f * 100).roundToInt() / 100.0

    private fun getCompass(): Int? {
        if (accelerometer != null && magneticField != null) {
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, accelerometer, magneticField)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                val azimut = orientation[0]
                return ((azimut / PI * 180).toInt() + 450) % 360
            }
        }
        return null
    }

    fun brakeChanged(isDown: Boolean) {
        brake = isDown
    }

    fun leftShiftChanged(isUp: Boolean): Byte {
        val newValue: Byte = (leftShift + if (isUp) 1 else -1).toByte()
        if (newValue in leftShiftValues) leftShift = newValue
        return leftShift
    }

    fun rightShiftChanged(isUp: Boolean): Byte {
        val newValue: Byte = (rightShift + if (isUp) 1 else -1).toByte()
        if (newValue in rightShiftValues) rightShift = newValue
        return rightShift
    }

    private fun getShift(): Byte = (rightShift + leftShift - 1).toByte()

    fun stop() {
        fileStream.close()
        locationManager.removeUpdates(this)
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}