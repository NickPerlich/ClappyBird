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

    var birdVelocity by mutableFloatStateOf(0f)

    // pipe state
    var pipeX by mutableFloatStateOf(800f)

    var gapY by mutableFloatStateOf(300f)

    val gapSize = 250f

    // game state
    var isGameOver by mutableStateOf(false)

    var onGameOver: (() -> Unit)? = null // navigation callback

    // physics constants
    private val gravity = 0.5f
    private val flapStrength = -10f

    // bird constants
    private val birdX = 100f
    private val birdSize = 40f

    // pipe constants
    private val pipeWidth = 60f

    // screen bounds
    private val screenTop = 0f
    private val screenBottom = 800f

    // game loop
    fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(16) // ~60 FPS (16ms per frame)

                // apply gravity to bird velocity
                birdVelocity += gravity

                // update bird position based on velocity
                birdY += birdVelocity

                // move pipe left
                pipeX -= 5f

                // reset pipe when it goes off screen
                if (pipeX < -100f) {
                    pipeX = 800f
                    gapY = (200..500).random().toFloat()  // Random gap position
                }

                // check for collisions
                checkCollisions()
            }
        }
    }

    // collision detection
    fun checkCollisions() {
        // check if bird hit top or bottom of screen
        if (birdY <= screenTop || birdY + birdSize >= screenBottom) {
            die()
            return
        }

        // check if bird hit a pipe
        if (birdX + birdSize > pipeX && birdX < pipeX + pipeWidth) {
            if (birdY < gapY || birdY + birdSize > gapY + gapSize) {
                die()
            }
        }
    }

    // die function
    fun die() {
        if (!isGameOver) {
            isGameOver = true
            onGameOver?.invoke() // trigger navigation to death screen
        }
    }

    // User action - flap
    fun flap() {
        if (!isGameOver) {
            birdVelocity = flapStrength  // set upward velocity
        }
    }

    // reset game
    fun resetGame() {
        birdY = 400f
        birdVelocity = 0f
        pipeX = 800f
        gapY = 300f
        isGameOver = false
    }
}