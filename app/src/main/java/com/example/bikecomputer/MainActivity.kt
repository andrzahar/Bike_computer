package com.example.bikecomputer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bikecomputer.ui.theme.BikeComputerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var playPauseButton: (Boolean) -> Unit
    private lateinit var volumeDownButton: (Boolean) -> Unit
    private lateinit var volumeUpButton: (Boolean) -> Unit

    private fun hideSystemUI() {

        actionBar?.hide()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                hide(WindowInsets.Type.systemGestures())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        event?.let {
            if (it.action == KeyEvent.ACTION_DOWN || it.action == KeyEvent.ACTION_UP) {
                when (it.keyCode) {
                    KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> { // 0 resistors
                        playPauseButton(it.action == KeyEvent.ACTION_DOWN)
                        return true
                    }
                    KeyEvent.KEYCODE_VOLUME_DOWN -> { // 2 resistors
                        volumeDownButton(it.action == KeyEvent.ACTION_DOWN)
                        return true
                    }
                    KeyEvent.KEYCODE_VOLUME_UP -> { // 4 resistors
                        volumeUpButton(it.action == KeyEvent.ACTION_DOWN)
                        return true
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            val viewModel = hiltViewModel<MainViewModel>()

            playPauseButton = viewModel::playPauseButton
            volumeDownButton = viewModel::volumeDownButton
            volumeUpButton = viewModel::volumeUpButton

            BikeComputerTheme {
                Content(
                    brake = viewModel.brake.value,
                    leftShift = viewModel.leftShift.value,
                    leftShiftUpReadiness = viewModel.leftShiftUpReadiness.value,
                    recommendedLeftShift = viewModel.recommendedLeftShift.value,
                    speed = viewModel.speed.value,
                    rightShift = viewModel.rightShift.value,
                    recommendedRightShift = viewModel.recommendedRightShift.value,
                    onShiftButtonsClick = viewModel::onShiftButtonsClick
                )
            }
        }

        hideSystemUI()
    }
}

@Composable
fun Content(
    brake: Boolean,
    leftShift: Byte,
    leftShiftUpReadiness: Float,
    recommendedLeftShift: Byte?,
    speed: Double,
    rightShift: Byte,
    recommendedRightShift: Byte?,
    onShiftButtonsClick: (Boolean, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    ArrowButton(
                        isRight = false,
                        isUp = true,
                        onShiftButtonsClick = onShiftButtonsClick
                    )
                    Row {
                        Text(
                            text = leftShift.toString(),
                            color = Color.Black,
                            fontSize = 60.sp,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        recommendedLeftShift?.let {
                            Text(
                                text = it.toString(),
                                color = Color.Black,
                                fontSize = 45.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    ArrowButton(
                        isRight = false,
                        isUp = false,
                        onShiftButtonsClick = onShiftButtonsClick
                    )
                    CircularProgressIndicator(progress = leftShiftUpReadiness)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = speed.toString(),
                        color = Color.Black,
                        fontSize = 60.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    ArrowButton(
                        isRight = true,
                        isUp = true,
                        onShiftButtonsClick = onShiftButtonsClick
                    )
                    Row {
                        recommendedRightShift?.let {
                            Text(
                                text = it.toString(),
                                color = Color.Black,
                                fontSize = 45.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(
                            text = rightShift.toString(),
                            color = Color.Black,
                            fontSize = 60.sp,
                            textAlign = TextAlign.End
                        )
                    }
                    ArrowButton(
                        isRight = true,
                        isUp = false,
                        onShiftButtonsClick = onShiftButtonsClick
                    )
                    if (brake) {
                        Icon(Icons.Outlined.Warning, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun ArrowButton(
    isRight: Boolean,
    isUp: Boolean,
    onShiftButtonsClick: (Boolean, Boolean) -> Unit
) = IconButton(
    modifier = Modifier.size(70.dp),
    onClick = { onShiftButtonsClick(isRight, isUp) }
) {
    Icon(
        if (isUp) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
        null,
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BikeComputerTheme {

    }
}