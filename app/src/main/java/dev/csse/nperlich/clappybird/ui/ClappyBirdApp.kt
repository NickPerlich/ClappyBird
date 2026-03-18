package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.csse.nperlich.clappybird.GameViewModel

object Routes {
    const val START = "start"
    const val GAME = "game"
    const val DEATH = "death"
}

@Preview
@Composable
fun ClappyBirdApp() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()
    val highScore by viewModel.highScore.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(
                onPlayClick = { username ->
                    viewModel.setUsername(username)
                    viewModel.resetGame()
                    navController.navigate(Routes.GAME)
                },
                highScore = highScore,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(Routes.GAME) {
            // set the game over callback
            viewModel.onGameOver = {
                navController.navigate(Routes.DEATH )
            }

            PlayScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }

        composable(Routes.DEATH) {
            val topScores by viewModel.topTenScores.collectAsState()

            DeathScreen(
                currentScore = viewModel.currentScore,
                topScores = topScores,
                onPlayAgainClick = {
                    viewModel.resetGame()
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                },
                onExitClick = {
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}