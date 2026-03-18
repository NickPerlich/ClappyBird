package dev.csse.nperlich.clappybird.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.csse.nperlich.clappybird.data.HighScore

@Composable
fun DeathScreen(
    modifier: Modifier = Modifier,
    currentScore: Int = 0,
    topScores: List<HighScore> = emptyList(),  // CHANGE: from single highScore to list
    onPlayAgainClick: () -> Unit = {},
    onExitClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Background()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "GAME OVER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Your Score: $currentScore",
                fontSize = 24.sp,
                color = Color.White
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "TOP 10 LEADERBOARD",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Leaderboard
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(topScores) { index, score ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${index + 1}. ${score.username}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${score.score}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Show message if no scores yet
                if (topScores.isEmpty()) {
                    item {
                        Text(
                            text = "No scores yet!",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp),
                            color = Color.White
                        )
                    }
                }
            }

            Button(onClick = onPlayAgainClick) {
                Text(
                    text = "Play Again",
                    color = Color.White
                )
            }

            Button(onClick = onExitClick) {
                Text(
                    text = "Exit",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeathScreenPreview() {
    DeathScreen(
        currentScore = 15,
        topScores = listOf(
            HighScore(1, 42, "Alice"),
            HighScore(2, 38, "Bob"),
            HighScore(3, 35, "Charlie"),
            HighScore(4, 30, "Diana"),
            HighScore(5, 25, "Eve")
        )
    )
}