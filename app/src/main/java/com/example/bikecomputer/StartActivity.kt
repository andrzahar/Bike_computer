package com.example.bikecomputer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bikecomputer.ui.theme.BikeComputerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity(), OnRequestPermissionsResultCallback {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var haveAllPermissions = false

    private var onPermissionUpdateListener: ((Boolean) -> Unit)? = null

    private fun updatePermissions(): Boolean {
        var res = true
        permissions.forEach {
            res = res && ActivityCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        onPermissionUpdateListener?.invoke(res)
        return res
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        updatePermissions()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        haveAllPermissions = updatePermissions()

        try {
            if (!haveAllPermissions) {
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            BikeComputerTheme {

                val viewModel = hiltViewModel<StartActivityViewModel>()

                onPermissionUpdateListener = viewModel::onPermissionUpdateListener

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(
                        haveAllPermissions = haveAllPermissions || viewModel.haveAllPermissions,
                        weatherSuccess = viewModel.weatherSuccess,
                        weatherState = viewModel.weatherState
                    ) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    haveAllPermissions: Boolean,
    weatherSuccess: Boolean,
    weatherState: String,
    onNextActivityClickListener: () -> Unit
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!haveAllPermissions) {
            Text(
                text = "У приложения нет прав для отслеживания геопозиции. " +
                        "Пожалуйста, перезапустите приложение и предоставьте недостающие права."
            )
        }
        Text(text = weatherState)
        Button(
            enabled = haveAllPermissions && weatherSuccess,
            onClick = onNextActivityClickListener
        ) {
            Text("Старт")
        }
        var openDialog by remember { mutableStateOf(false) }
        Button(
            onClick = { openDialog = true }
        ) {
            Text("О программе")
        }
        if (openDialog) {
            AlertDialog(
                onDismissRequest = {
                    openDialog = false
                },
                title = { Text(text = "О программе") },
                text = {
                    Text(
                        "Интеллектуальная программа-помощник выбора передачи на велосипеде\n" +
                                "Авторы: Семенов Анатолий, Захаров Андрей\n"+
                                "2023 год"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        openDialog = false
                    }) {
                        Text("Закрыть")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BikeComputerTheme {

    }
}