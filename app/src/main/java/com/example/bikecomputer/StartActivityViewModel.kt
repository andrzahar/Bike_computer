package com.example.bikecomputer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikecomputer.common.Resource
import com.example.bikecomputer.weather.HourRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StartActivityViewModel @Inject constructor(
    private val hourRepository: HourRepository
) : ViewModel() {

    var haveAllPermissions by mutableStateOf(false)
        private set

    var weatherSuccess by mutableStateOf(false)
        private set

    var weatherState by mutableStateOf("")
        private set

    init {
        hourRepository.update().onEach {
            when (it) {
                is Resource.Loading -> {
                    weatherState = "Загрузка погоды..."
                    weatherSuccess = false
                }
                is Resource.Error -> {
                    weatherState = "Ошибка загрузки погоды: ${it.message}"
                    weatherSuccess = false
                }
                is Resource.Success -> {
                    weatherState = "Погода успешно обновлена"
                    weatherSuccess = true
                }
            }
        }.launchIn(viewModelScope)

    }

    fun onPermissionUpdateListener(haveAllPermissions: Boolean) {
        this.haveAllPermissions = haveAllPermissions
    }
}