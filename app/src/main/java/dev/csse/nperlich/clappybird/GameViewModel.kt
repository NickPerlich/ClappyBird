package dev.csse.nperlich.clappybird

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.csse.nperlich.clappybird.data.GameDatabase
import dev.csse.nperlich.clappybird.data.HighScore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = GameDatabase.getDatabase(application)
    private val highScoreDao = database.highScoreDao()

    // high score flow
    val highScore: StateFlow<HighScore?> = MutableStateFlow(null)

    private var gameLoopJob: Job? = null // track the game loop

    init {
        // load high score when viewmodel is created
        viewModelScope.launch {
            highScoreDao.getHighScore().collect { score ->
                (highScore as MutableStateFlow).value = score
            }
        }
    }

    // bird state
    var birdY by mutableFloatStateOf(400f)

    var birdVelocity by mutableFloatStateOf(0f)

    // pipe state
    var pipeX by mutableFloatStateOf(800f)

    var gapY by mutableFloatStateOf(300f)

    val gapSize = 250f

    // game state
    var isGameOver by mutableStateOf(false)

    var currentScore by mutableStateOf(0)

    var currentUsername by mutableStateOf("")

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

    // track if we have scored this pipe yet
    private var hasScored = false

    // set username for this game session
    fun setUsername(username: String) {
        currentUsername = username
    }

    // game loop
    fun startGameLoop() {
        // cancel any existing game loop
        gameLoopJob?.cancel()

        // start new game loop
        gameLoopJob = viewModelScope.launch {
            while (true) {
                delay(16) // ~60 FPS (16ms per frame)

                // apply gravity to bird velocity
                birdVelocity += gravity

                // update bird position based on velocity
                birdY += birdVelocity

                // move pipe left
                pipeX -= 5f

                // check if bird passed the pipe
                if (!hasScored && birdX > pipeX + pipeWidth) {
                    currentScore++
                    hasScored = true
                }

                // reset pipe when it goes off screen
                if (pipeX < -100f) {
                    pipeX = 800f
                    gapY = (200..500).random().toFloat()  // Random gap position
                    hasScored = false // reset scoring for new pipe
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
            gameLoopJob?.cancel() // stop the game loop

            // check and save high score
            viewModelScope.launch {
                val currentHighScore = highScore.value
                if (currentHighScore == null || currentScore > currentHighScore.score) {
                    // new high score
                    highScoreDao.updateHighScore(
                        HighScore(
                            id = 1,
                            score = currentScore,
                            username = currentUsername
                        )
                    )
                }
            }

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
        gameLoopJob?.cancel()
        birdY = 400f
        birdVelocity = 0f
        pipeX = 800f
        gapY = 300f
        isGameOver = false
        currentScore = 0
        hasScored = false
    }
}