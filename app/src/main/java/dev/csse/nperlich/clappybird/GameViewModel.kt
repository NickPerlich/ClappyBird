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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = GameDatabase.getDatabase(application)
    private val highScoreDao = database.highScoreDao()

    // high score flow
    val highScore: StateFlow<HighScore?> = MutableStateFlow(null)

    // top 10 scores flow
    val topTenScores: StateFlow<List<HighScore>> = MutableStateFlow(emptyList())

    private var gameLoopJob: Job? = null // track the game loop

    init {
        // load high score when viewmodel is created
        viewModelScope.launch {
            highScoreDao.getHighScore().collect { score ->
                (highScore as MutableStateFlow).value = score
            }
        }

        viewModelScope.launch {
            highScoreDao.getTop10Scores().collect() { scores ->
                (topTenScores as MutableStateFlow).value = scores
            }
        }
    }

    // bird state
    var birdY by mutableFloatStateOf(400f)

    var birdVelocity by mutableFloatStateOf(0f)

    // pipe state
    var pipeX by mutableFloatStateOf(500f)

    var gapY by mutableFloatStateOf(300f)

    var pipe2X by mutableFloatStateOf(800f)

    var gap2Y by mutableFloatStateOf(300f)

    val gapSize = 250f

    // game state
    var isGameOver by mutableStateOf(false)

    var hasGameStarted by mutableStateOf(false)

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
    private var hasScored1 = false
    private var hasScored2 = false

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

                if (!isGameOver && hasGameStarted) {
                    // apply gravity to bird velocity
                    birdVelocity += gravity

                    // update bird position based on velocity
                    birdY += birdVelocity

                    // move pipes left
                    pipeX -= 5f
                    pipe2X -= 5f

                    // check if bird passed the first pipe
                    if (!hasScored1 && birdX > pipeX + pipeWidth) {
                        currentScore++
                        hasScored1 = true
                    }

                    // check if bird passed second pipe
                    if (!hasScored2 && birdX > pipe2X + pipeWidth) {
                        currentScore++
                        hasScored2 = true  // reset for next cycle
                    }

                    val pipeSpacing = 400f

                    // reset pipe 1 when it goes off screen
                    if (pipeX < -100f) {
                        pipeX = pipe2X + pipeSpacing  // Spawn relative to pipe 2
                        gapY = (200..500).random().toFloat()
                        hasScored1 = false
                    }

                    // reset pipe 2 when it goes off screen
                    if (pipe2X < -100f) {
                        pipe2X = pipeX + pipeSpacing  // Spawn relative to pipe 1
                        gap2Y = (200..500).random().toFloat()
                        hasScored2 = false
                    }

                    // check for collisions
                    checkCollisions()
                }
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

        // check if bird hit first pipe
        if (birdX + birdSize > pipeX && birdX < pipeX + pipeWidth) {
            if (birdY < gapY || birdY + birdSize > gapY + gapSize) {
                die()
            }
        }

        // check if bird hit second pipe
        if (birdX + birdSize > pipe2X && birdX < pipe2X + pipeWidth) {
            if (birdY < gap2Y || birdY + birdSize > gap2Y + gapSize) {
                die()
            }
        }
    }

    // die function
    fun die() {
        if (!isGameOver) {
            isGameOver = true
            gameLoopJob?.cancel() // stop the game loop

            // save score to leaderboard
            viewModelScope.launch {
                highScoreDao.insertScore(
                    HighScore(
                        score = currentScore,
                        username = currentUsername
                    )
                )
                // Clean up - keep only top 10
                highScoreDao.deleteOldScores()
            }

            onGameOver?.invoke() // trigger navigation to death screen
        }
    }

    // User action - flap
    fun flap() {
        if (!isGameOver) {
            if (!hasGameStarted) {
                hasGameStarted = true // first clap starts game
            }
            birdVelocity = flapStrength  // set upward velocity
        }
    }

    // reset game
    fun resetGame() {
        gameLoopJob?.cancel()
        birdY = 400f
        birdVelocity = 0f
        pipeX = 500f
        gapY = 300f
        pipe2X = 800f
        gap2Y = 300f
        isGameOver = false
        hasGameStarted = false
        currentScore = 0
        hasScored1 = false
        hasScored2 = false
    }
}