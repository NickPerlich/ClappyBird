package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            PlayScreen(
                modifier = Modifier.fillMaxSize(),
                onDieClick = {
                    navController.navigate(Routes.DEATH)
                }
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