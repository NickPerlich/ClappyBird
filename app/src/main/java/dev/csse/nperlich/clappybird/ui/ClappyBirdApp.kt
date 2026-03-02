package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(
                onPlayClick = {
                    navController.navigate(Routes.GAME)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(Routes.GAME) {
            val viewModel: GameViewModel = viewModel()

            viewModel.onGameOver = {
                navController.navigate(Routes.DEATH )
            }

            PlayScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }
        composable(Routes.DEATH) {
            DeathScreen(
                modifier = Modifier.fillMaxSize(),
                onRestartClick = {
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                },
            )
        }
    }
}