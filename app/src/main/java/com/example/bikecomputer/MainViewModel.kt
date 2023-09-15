package com.example.bikecomputer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikecomputer.weather.HourRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainWorker: MainWorker
) : ViewModel() {

    private val _leftShiftUpReadiness = mutableStateOf(0.0f)
    val leftShiftUpReadiness: State<Float> = _leftShiftUpReadiness

    private val longPressCount = 10

    private var leftLongPressCounter = 0
        set(value) {
            _leftShiftUpReadiness.value = value.toFloat() / longPressCount.toFloat()
            field = value
        }
    private var rightLongPressCounter = 0
        set(value) {
            _leftShiftUpReadiness.value = value.toFloat() / longPressCount.toFloat()
            field = value
        }

    private val _rightShift = mutableStateOf<Byte>(0)
    val rightShift: State<Byte> = _rightShift

    private val _leftShift = mutableStateOf<Byte>(0)
    val leftShift: State<Byte> = _leftShift

    private val _recommendedRightShift = mutableStateOf<Byte?>(null)
    val recommendedRightShift: State<Byte?> = _recommendedRightShift

    private val _recommendedLeftShift = mutableStateOf<Byte?>(null)
    val recommendedLeftShift: State<Byte?> = _recommendedLeftShift

    private val _brake = mutableStateOf(false)
    val brake: State<Boolean> = _brake

    private val _speed = mutableStateOf(.0)
    val speed: State<Double> = _speed

    init {
        mainWorker.start().onEach {
            _leftShift.value = it.leftShift
            _rightShift.value = it.rightShift
            _speed.value = it.speed
            _recommendedLeftShift.value = it.recommendedLeftShift
            _recommendedRightShift.value = it.recommendedRightShift
        }.launchIn(viewModelScope)
        /*hourRepository.update().onEach {
            Log.d("ttt", "${it.message}")
        }.launchIn(viewModelScope)*/
    }

    fun playPauseButton(isActionDown: Boolean) {
        _brake.value = isActionDown
        mainWorker.brakeChanged(isActionDown)
    }

    fun volumeDownButton(isActionDown: Boolean) {
        if (isActionDown) {
            leftLongPressCounter++
        } else {
            if (leftLongPressCounter >= longPressCount) {
                leftShiftChanged(false)
            } else {
                rightShiftChanged(false)
            }

            leftLongPressCounter = 0
        }
    }

    fun volumeUpButton(isActionDown: Boolean) {
        if (isActionDown) {
            rightLongPressCounter++
        } else {
            if (rightLongPressCounter >= longPressCount) {
                leftShiftChanged(true)
            } else {
                rightShiftChanged(true)
            }

            rightLongPressCounter = 0
        }
    }

    fun onShiftButtonsClick(isRight: Boolean, isUp: Boolean) {
        if (isRight) rightShiftChanged(isUp)
        else leftShiftChanged(isUp)
    }

    private fun leftShiftChanged(isUp: Boolean) {
        _leftShift.value = mainWorker.leftShiftChanged(isUp)
    }

    private fun rightShiftChanged(isUp: Boolean) {
        _rightShift.value = mainWorker.rightShiftChanged(isUp)
    }

    override fun onCleared() {
        mainWorker.stop()
        super.onCleared()
    }
}