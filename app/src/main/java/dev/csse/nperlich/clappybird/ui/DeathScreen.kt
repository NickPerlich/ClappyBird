package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.csse.nperlich.clappybird.data.HighScore

@Composable
fun DeathScreen(
    currentScore: Int = 0,
    highScore: HighScore? = null,
    modifier: Modifier = Modifier,
    onRestartClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "GAME OVER",
                fontSize = 32.sp
            )

            Text(
                text = "Your Score: $currentScore",
                fontSize = 24.sp
            )

            highScore?.let {
                Text(
                    text = "High Score: ${it.score} by ${it.username}",
                    fontSize = 20.sp
                )
            }

            Button(onClick = onRestartClick) {
                Text("RESTART")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeathScreenPreview() {
    DeathScreen()
}