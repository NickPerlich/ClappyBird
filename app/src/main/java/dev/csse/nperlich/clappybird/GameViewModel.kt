package dev.csse.nperlich.clappybird

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    // bird state
    var birdY by mutableFloatStateOf(400f)

    // pipe state
    var pipeX by mutableFloatStateOf(800f)

    var gapY by mutableFloatStateOf(300f)

    val gapSize = 200f

    // game loop
    fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(16) // ~60 FPS (16ms per frame)

                // move pipe left
                pipeX -= 5f

                // reset pipe when it goes off screen
                if (pipeX < -100f) {
                    pipeX = 800f
                    gapY = (200..600).random().toFloat()  // Random gap position
                }
            }
        }
    }

    // User action - flap
    fun flap() {
        birdY -= 50f  // Move bird up
    }
}